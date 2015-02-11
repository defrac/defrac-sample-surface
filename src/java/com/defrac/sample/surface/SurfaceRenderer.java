package com.defrac.sample.surface;

import defrac.display.GLSurface;
import defrac.display.event.UIEventManager;
import defrac.geom.Point;
import defrac.gl.*;
import defrac.resource.StringResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.defrac.sample.surface.GLUtil.createShader;
import static com.defrac.sample.surface.GLUtil.linkProgram;

class SurfaceRenderer implements GLSurface.Renderer {
  UIEventManager eventManager;
  Point mousePos = new Point();
  String shaderCode;
  boolean reload;
  private float t = 0.0f;
  GLProgram program;
  GLBuffer buffer;
  GLUniformLocation time, mouse, resolution;

  SurfaceRenderer(UIEventManager eventManager) {
    this.eventManager = eventManager;

    // Load a StringResource which contains the code of the shader
    StringResource shaderResource =
        StringResource.from("shader.glsl");

    shaderResource.listener(new StringResource.SimpleListener() {
      @Override
      public void onResourceComplete(@Nonnull StringResource resource, @Nonnull String content) {
        onShaderComplete(content);
      }

      @Override
      public void onResourceUpdate(@Nonnull StringResource resource, @Nonnull String content) {
        //listen for onUpdate so we an hot-swap the code!
        onShaderComplete(content);
      }
    });

    shaderResource.load();
  }

  // Listener for when the code has been loaded or updated
  void onShaderComplete(String code) {
    shaderCode = code;
    reload = true;
  }

  // This method is called by the rendering system every time the
  // surface is being rendered
  //
  // Note that we are running within the renderer system so we
  // always get a fresh state, which means we have to bind buffers again
  // and cannot expect the OpenGL state to be the same as when we left
  // this function
  //
  // Note: The rendering system tries to get rid of redundant calls!
  @Override
  public void onGLSurfaceRender(@Nonnull final GLSurface surface,
                             @Nonnull final GL gl,
                             @Nonnull final GLFrameBuffer frameBuffer,
                             @Nullable final GLRenderBuffer renderBuffer,
                             @Nonnull final GLTexture surfaceTexture,
                             final float width, final float height,
                             final int viewportWidth, final int viewportHeight,
                             final boolean transparent) {
    if(null == shaderCode) {
      //No code loaded yet, do nothing
      return;
    }

    // We have some code to load
    if(reload) {
      reload = false;

      if(null != program) {
        // We already have a working program, get rid of it
        gl.deleteProgram(program);
        program = null;
        time = null;
        mouse = null;
        resolution = null;
      }

      // Create a new vertex and fragment shader
      GLShader vertexShader = createShader(gl, VERTEX_SHADER_SOURCE, GL.VERTEX_SHADER);
      if(null == vertexShader) {
        // Whoops, error!
        return;
      }

      GLShader fragmentShader = createShader(gl, shaderCode, GL.FRAGMENT_SHADER);
      if(null == fragmentShader) {
        // Whoops, error!
        return;
      }

      // Create a new program
      program = gl.createProgram();

      // Link the program, this will do some cleanup of the shaders for us
      if(!linkProgram(gl, program, vertexShader, fragmentShader, new String[] { "position" })) {
        gl.deleteProgram(program);
        program = null;
        return;
      }

      // Finally cache the location of some uniforms
      time = gl.getUniformLocation(program, "time");
      mouse = gl.getUniformLocation(program, "mouse");
      resolution = gl.getUniformLocation(program, "resolution");
      t = 0.0f;
    }

    if(null == program) {
      // We have some code, but no program which means there
      // was a syntax error
      return;
    }

    if(null == buffer) {
      // Lazy setup of our buffer
      buffer = gl.createBuffer();
      gl.bindBuffer(GL.ARRAY_BUFFER, buffer);
      // Create two triangles that fill the whole viewport
      gl.bufferData(GL.ARRAY_BUFFER, new float[] { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f }, 0, 12, GL.STATIC_DRAW);
    } else {
      // We already have a buffer and need to bind it!
      gl.bindBuffer(GL.ARRAY_BUFFER, buffer);
    }

    // Fetch the position of pointer 0 (mouse or first touch point)
    eventManager.pointerPos(mousePos, /*index=*/0);

    // Convert to local coordinates (since we scale it!)
    surface.globalToLocal(mousePos);

    // Actual rendering code:
    // - clear screen
    // - use gpu program
    // - update uniform variables
    // - draw the triangles
    gl.clear(GL.COLOR_BUFFER_BIT);
    gl.useProgram(program);
    gl.uniform1f(time, t);
    gl.uniform2f(mouse, mousePos.x, mousePos.y);
    gl.uniform2f(resolution, width, height);
    gl.enableVertexAttribArray(0);
    gl.vertexAttribPointer(0, 2, GL.FLOAT, false, 0, 0);
    gl.drawArrays(GL.TRIANGLES, 0, 6);
    t += 0.01f;
  }

  // The code of the vertex shader
  private static final String VERTEX_SHADER_SOURCE =
      ""+
          "attribute vec3 position;"+
          "void main() {"+
          "  gl_Position = vec4(position, 1.0);"+
          "}";
}
