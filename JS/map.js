var countryElements = document.getElementById("countries").childNodes;
var countryCount = countryElements.length;
for (var i = 0; i < countryCount; i++) {
  countryElements[i].onclick = function () {
    alert("You clicked on " + this.getAttribute("data-name"));
  };
}

// const NAV_MAP = {
//     // +
//     187: {
//       act: "zoom",
//       dir: 1,
//       name: "in",
//     },
//     // -
//     189: {
//       act: "zoom",
//       dir: -1,
//       name: "out",
//     },
//     // =>
//     39: {
//       act: "move",
//       dir: 0.5,
//       name: "right",
//       axis: 0,
//     },
//     // <=
//     37: {
//       act: "move",
//       dir: -0.5,
//       name: "left",
//       axis: 0,
//     },
//     // up
//     38: {
//       act: "move",
//       dir: -0.5,
//       name: "up",
//       axis: 1,
//     },
//     // down
//     40: {
//       act: "move",
//       dir: 0.5,
//       name: "down",
//       axis: 1,
//     },
//   },
//   _svg = document.querySelector("svg"),
//   _viewbox = _svg
//     .getAttribute("viewBox")
//     .split(" ")
//     .map((c) => +c),
//   dmax = _viewbox.slice(2),
//   wmin = 8,
//   nf = 60;

// let nav = null;
// let tg = Array(4);
// let f = 0;
// let rID = null;

// function update() {
//   let k = ++f / nf;
//   let j = 1 - k;
//   let cvb = _viewbox.slice();
//   if (nav.act === "zoom") {
//     for (let i = 0; i < 4; i++) {
//       cvb[i] = j * _viewbox[i] + k * tg[i];
//     }
//   } else if (nav.act === "move") {
//     cvb[nav.axis] = j * _viewbox[nav.axis] + k * tg[nav.axis];
//   }
//   _svg.setAttribute("viewBox", cvb.join(" "));

//   if (!(f % nf)) {
//     cancelAnimationFrame(rId);
//     rID = null;
//     nav = null;
//     f = 0;
//     tg = Array(4);
//     _viewbox.splice(0, 4, ...cvb);
//     return;
//   }

//   rId = requestAnimationFrame(update);
// }

// addEventListener(
//   "keyup",
//   (e) => {
//     if (e.keyCode in NAV_MAP) {
//       nav = NAV_MAP[e.keyCode];
//       if (nav.act === "zoom") {
//         if (
//           (nav.dir === -1 && _viewbox[2] >= dmax[0]) ||
//           (nav.dir === 1 && _viewbox[2] <= wmin)
//         ) {
//           console.log(`cannot zoom`);
//           return;
//         }
//         for (let i = 0; i < 2; i++) {
//           tg[i + 2] = _viewbox[i + 2] / Math.pow(2, nav.dir);
//           tg[i] = 0.5 * (dmax[i] - tg[i + 2]);
//         }
//       } else if (nav.act === "move") {
//         if (
//           (nav.dir === -0.5 && _viewbox[nav.axis] <= 0) ||
//           (nav.dir === 0.5 &&
//             _viewbox[nav.axis] >= dmax[nav.axis] - _viewbox[nav.axis + 2])
//         ) {
//           console.log(`cannot move`);
//           return;
//         }
//         tg[nav.axis] =
//           _viewbox[nav.axis] + 0.5 * nav.dir * _viewbox[nav.axis + 2];
//       }
//     }
//     update();
//   },
//   false
// );

const map = document.querySelector(".container");
console.log(map);
let zoom = 1;
const zoom_speed = 0.2;

document.addEventListener("wheel", (e) => {
  if (e.deltaY > 0) {
    map.style.transform = `scale(${(zoom -= zoom_speed)})`;
  } else {
    map.style.transform = `scale(${(zoom += zoom_speed)})`;
  }
});
