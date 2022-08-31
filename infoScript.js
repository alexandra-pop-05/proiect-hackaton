//learn more/less button function
function MoreLess(){
    var invisibleText = document.getElementById("show-more");
    var btnTextMore = document.getElementById("btn-more");

  if (invisibleText.style.display === "none") {
    invisibleText.style.display = "inline";
    btnTextMore.style.display = "none";

  } else {
    invisibleText.style.display = "none";
    btnTextMore.style.display = "inline";
  }
}