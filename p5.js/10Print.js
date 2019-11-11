var i = 0
var k = 0
function setup() {
  createCanvas(400, 400);
  background(220);
}

function draw() {
  let rand = random()
  if (rand < 0.5) {
    line(10*i, 10*k, 10*(i+1), 10*(k+1))
  }
  else {
    line(10*i, 10*(k+1), 10*(i+1), 10*k)
  }
  i++
  if (i >= width/10) {
    i = 0
    k++
  }
  if (k >= height/10) {
    noLoop()
  }
}
