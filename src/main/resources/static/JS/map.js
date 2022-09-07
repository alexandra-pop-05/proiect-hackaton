var countryElements = document.getElementById("countries").childNodes;
var countryCount = countryElements.length;
var modal = document.getElementById("myModal");
var modalHeader = document.getElementById("modal-header");
var modalBody = document.getElementById("modal-body");
var HttpClient = function () {
  this.get = function (aUrl, aCallback) {
    var anHttpRequest = new XMLHttpRequest();
    anHttpRequest.onreadystatechange = function () {
      if (anHttpRequest.readyState === 4 && anHttpRequest.status === 200)
        aCallback(anHttpRequest.responseText);
    }
    anHttpRequest.open("GET", aUrl, true);
    anHttpRequest.send(null);
  }
}

for (var i = 0; i < countryCount; i++) {
  countryElements[i].onclick = function getName() {
    modal.style.display = "block";

    modalHeader.innerHTML += this.getAttribute("data-name");
    //var data = "<p> Salut <p>";

    var theurl = 'http://localhost:8080/countries/measurements?countryName=' + this.getAttribute("data-name");
    var client = new HttpClient();
    client.get(theurl, function (response){
      var response1 = JSON.parse(response);
      var aqi = parseInt(response1.countryAqi);
      if(aqi >= 0 && aqi <= 19){
        modalHeader.style.background = "#43ff6f";
      }
      else if(aqi >= 20 && aqi <= 49){
        modalHeader.style.background = "#9ce21d";
      }
      else if(aqi >= 50 && aqi <= 99){
        modalHeader.style.background = "#cbc100";
      }
      else if(aqi >= 100 && aqi <= 149){
        modalHeader.style.background = "#eb9b00";
      }
      else if(aqi >= 150 && aqi <= 249){
        modalHeader.style.background = "#fd7114";
      }
      else {
        modalHeader.style.background = "#ff4343";
      }
      modalBody.innerHTML +="<p>Average Aqi: <strong>" + response1.countryAqi + "</strong><br>"
                          + "Average Temperature: <strong>" + response1.countryTemp + " \u2103</strong><br>"
                          + "Average Pressure: <strong>" + response1.countryPressure + " mb</strong><br>"
                          + "Average Humidity: <strong>" + response1.countryHumidity + "%</strong><br>"
                          + "Average Wind: <strong>" + response1.countryWind + " km/h</strong><br></p>";
      modalBody.innerHTML +="Top 3 Cities Highest Aqi: <br>";
      for(var i = 1; i <= response1.top3CitiesHighAqis.length; i++){
        modalBody.innerHTML +=i + ") <strong>" + response1.top3CitiesHighAqis[i - 1].cityName + "</strong> - <strong>" + response1.top3CitiesHighAqis[i - 1].aqi + "</strong>&emsp;";
      }
      modalBody.innerHTML +="<br>Top 3 Cities Lowest Aqi: <br>";
      for(var i = 1; i <= response1.top3CitiesHighAqis.length; i++){
        modalBody.innerHTML +=i + ") <strong>" + response1.top3CitiesLowAqis[i - 1].cityName + "</strong> - <strong>" + response1.top3CitiesLowAqis[i - 1].aqi + "</strong>&emsp;";
      }
      modalBody.innerHTML += "<br>Top 3 Cities Highest Temperature: <br>";
      for(var i = 1; i <= response1.top3CitiesHighAqis.length; i++){
        modalBody.innerHTML += i + ") <strong>" + response1.top3CitiesHighTemp[i - 1].cityName + "</strong> - <strong>" + response1.top3CitiesHighTemp[i - 1].temperature + " \u2103</strong>&emsp;";
      }
      modalBody.innerHTML += "<br>Top 3 Cities Lowest Temperature: <br>";
      for(var i = 1; i <= response1.top3CitiesHighAqis.length; i++){
        modalBody.innerHTML += i + ") <strong>" + response1.top3CitiesLowTemp[i - 1].cityName + "</strong> - <strong>" + response1.top3CitiesLowTemp[i - 1].temperature + " \u2103</strong>&emsp;";
      }
      modalBody.innerHTML += "<br>Last updated: <strong>" + response1.lastUpdatedDateTime + " hours ago</strong>";
    });
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

    var svg = d3.select("svg");
    var theurl = 'http://localhost:8080/countries/recyclePoints?countryName=' + e.target.getAttribute("data-name");
    var client = new HttpClient();

    client.get(theurl, function (response) {
      var response1 = JSON.parse(response);
      for(var i = 0; i < response1.recyclePoints.length; i++){
         svg.append("circle").attr("cx", 0).attr("cy", 0).attr("r", 1).style("fill", "purple").attr("transform", "translate(" + response1.recyclePoints[i].geoLatitude + 1000 + "," + response1.recyclePoints[i].geoLongitude + 1000 + ")");
      }
    });
  });
  link.addEventListener("mouseout", (e) => {
    description.classList.remove("active");
    d3.selectAll("circle").remove();

  });
  link.addEventListener("mousemove", (e) => {
    description.style.left = e.pageX + "px";
    description.style.top = e.pageY - 70 + "px";
  });
});
