function setup() {
  createCanvas(400, 400);
}

function draw() {
  background(220);
  stroke(0)
  strokeWeight(2.5)
  for (i=0; i<height; i++) {
    for (k=0; k<mouseX; k=k+50) {
      fill(random(0, 255), random(0, 255), random(0, 255))
      ellipse(k, 50*i, 25, 25)
    }
  }
}
