let max_points = 0
let r = 100
let N = 400
let a = 0
let pi = 0
function setup() {
  createCanvas(N, N);
}

function draw() {
  // frameRate(1);
  background(220);
  for (i=0; i<max_points; i++) {
    point(sin(a)*r+N/2, cos(a)*r+N/2)
    line(sin(a)*r+N/2, cos(a)*r+N/2, sin(a+2*PI/max_points)*r+N/2, cos(a+2*PI/max_points)*r+N/2)
    a += 2*PI/max_points
  }
  pi = max_points * dist(sin(a)*r+N/2, cos(a)*r+N/2, sin(a+2*PI/max_points)*r+N/2, cos(a+2*PI/max_points)*r+N/2) / 200
  // if (max_points % 10 == 0) {
  //   print(pi)
  // }
  max_points += 1
  if (max_points > 1000) {
    noLoop()
    print(pi)
  }
}
