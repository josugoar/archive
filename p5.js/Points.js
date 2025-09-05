var r_min = 15;
var r_max = 50;
var coordinates = [];
var n_p = 0;

var i = 0;
var max_i = 500;

var R = 250;
var pointer_r = 5;

function setup() {
  createCanvas(400, 400);
  background(220);
}

function draw() {

  frameRate(100);
  noStroke();

  let p = {
    x: random(0, width),
    y: random(0, height),
    do: true,
  };

  for (let count = 0; count < coordinates.length; count++) {
    let key = coordinates[count];
      let d = dist(key[0], key[1], p.x, p.y);
      if (d < r_min) {
        p.do = false;
        break;
      }
  }

  if (p.do == true) {
    stroke(0);
    fill(random(0, 255), random(0, 255), random(0, 255), random(0, 255));
    ellipse(p.x, p.y, r_min, r_min);
    n_p++;
    coordinates = coordinates.concat([[p.x, p.y]]);
  }

  if (i == max_i) {
    noLoop();
    console.log("Number of points: " + n_p);
  } else {
    i++;
  }
}

let circles = 0;
function mouseClicked() {

  let x = mouseX;
  let y = mouseY;

  if ((i == max_i) && (circles == 0)) {
    stroke(0, 0, 0, 100);
    strokeWeight(5);
    fill(255, 255, 255, 50);
    ellipse(x, y, R, R);

    let inside = 0;
    for (let count = 0; count < coordinates.length; count++) {
      let key = coordinates[count];
        let d = dist(key[0], key[1], x, y);

        if (d < R/2) {

          strokeWeight(1);
          fill(0, 0, 0, 0);
          line(key[0], key[1], x, y);

          fill(0, 0, 0, 255);
          ellipse(x, y, pointer_r, pointer_r);
          ellipse(key[0], key[1], pointer_r, pointer_r);

          inside++;
        }
    }
    console.log("Points inside: " + inside);
    circles++;
  }
}
