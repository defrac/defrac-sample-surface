uniform vec2 mouse;
uniform vec2 resolution;
uniform float time;

const float speed = 0.75;
const float radius = 0.00625;

vec3 circlePos(float time, vec2 mp, vec4 hash) {
  //calc pos and radius of circle
  float a = hash.w * 10.0 + speed * (1.0 + hash.x) * time;
  //float b = hash.z * 10.0 + speed * (1.0 + hash.y) * time;
  float r = (hash.x * 0.0625 + hash.w * 0.0625) - hash.x * 0.00625 - hash.y * 0.00625;
  vec2 p = vec2(mp.x / (1.0 + 100.0 * r), mp.y / (1.0 + 100.0 * r)) +
    (hash.z * 0.7 + hash.w * 0.5 + hash.y * 0.4 + 0.001) * vec2(cos(a), sin(a));
  return vec3(p.x, p.y, r);
}

vec3 intersectCircle(vec3 col, vec2 uv, vec3 pos, vec4 hash) {
  vec3 res = col;
  float r = pos.z + radius;
  float d = distance(pos.xy, uv);
  if(d < r) {
    res += (1.5 - pos.z * 10.0) * smoothstep(0.8, 0.5, d/r) *
      vec3(0.25 + hash.x * 0.5 + hash.w * 0.25,
           0.20 + hash.y * 0.5 + hash.z * 0.0625,
           0.0625 + hash.z * 0.5);
  }
  return res;
}

//credits to iq for the hash function
vec4 hash4(float n) {
  return fract(sin(vec4(n,n+1.0,n+2.0,n+3.0))*vec4(43758.5453123,22578.1459123,19642.3490423,85124.04831));
}

void main( void )
{
  // calc aspect ratio and convert to uv coords (from -1 to 1)
  float aspect = resolution.x/resolution.y;
  vec2 mp = -1.0 + 2.0 * mouse / resolution;
  vec2 uv = -1.0 + 2.0 * gl_FragCoord.xy / resolution;
  vec3 col = vec3(0.1) + 0.05*uv.y;
  uv.x *= aspect;
  mp.x *= aspect;
  mp.y *= -1.0;

  for(int i = 0; i < 50; i++) {
    // draw some circles
    vec4 hash = hash4(float(i) * 13.13);
    col = intersectCircle(col, uv, circlePos(time, mp, hash), hash);
  }

  //some color grading
  col.x *= 0.6;
  col.y *= 0.9;
  col.z *= 1.5;

  gl_FragColor = vec4(col, 1.0);
}
