var countryElements = document.getElementById("countries").childNodes;
var countryCount = countryElements.length;
var modal = document.getElementById("myModal");
var modalHeader = document.getElementById("modal-header");
var modalBody = document.getElementById("modal-body");

for (var i = 0; i < countryCount; i++) {
  countryElements[i].onclick = function getName() {
    modal.style.display = "block";
    modalHeader.innerHTML += this.getAttribute("data-name");
    var data = "<p> Salut <p>";
    modalBody.innerHTML += data;
  };
}
// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
  if (event.target == modal) {
    modalHeader.innerHTML = "";
    modalBody.innerHTML = "";
    modal.style.display = "none";
  }
};

const map = document.querySelector(".body");
console.log(map);
var scale = 1,
  panning = false,
  pointX = 0,
  pointY = 0,
  start = { x: 0, y: 0 };

function setTransform() {
  map.style.transform =
    "translate(" + pointX + "px," + pointY + "px) scale(" + scale + ")";
}

function zoom() {
  map.style.transform = "scale(" + scale + ")";
  console.log(map.style.transform);
}

map.addEventListener("mousedown", (e) => {
  e.preventDefault();
  start = { x: e.clientX - pointX, y: e.clientY - pointY };
  panning = true;
});

map.addEventListener("mouseup", (e) => {
  e.preventDefault();
  e.stopPropagation();
  panning = false;
});

map.addEventListener("mousemove", (e) => {
  e.preventDefault();
  if (!panning) {
    return;
  }
  pointX = e.clientX - start.x;
  pointY = e.clientY - start.y;
  setTransform();
});

map.addEventListener("wheel", (e) => {
  e.preventDefault();
  var delta = e.wheelDelta ? e.wheelDelta : -e.deltaY;
  if (delta > 0) {
    scale *= 1.2;
    applyScale(e);
  } else {
    if (scale / 1.2 >= 1) {
      scale /= 1.2;
      applyScale(e);
    }
  }
});

function applyScale(e) {
  var xs = (e.clientX - pointX) / scale,
    ys = (e.clientY - pointY) / scale;
  pointX = e.clientX - xs * scale;
  pointY = e.clientY - ys * scale;
  zoom();
}

const description = document.querySelector(".description");
const country = document.querySelector(".countries");
const path = country.querySelectorAll("path");

path.forEach((link) => {
  link.addEventListener("mouseover", (e) => {
    e.preventDefault();
    description.classList.add("active");
    description.innerHTML = e.target.getAttribute("data-name");
  });
  link.addEventListener("mouseout", (e) => {
    description.classList.remove("active");
  });
  link.addEventListener("mousemove", (e) => {
    description.style.left = e.pageX + "px";
    description.style.top = e.pageY - 70 + "px";
  });
});
