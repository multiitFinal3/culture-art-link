$(document).ready(function() {
    initMap();
});

function initMap() {
    const museum = { lat: 37.5241668, lng: 126.9803166 }; // 국립중앙박물관 좌표
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 15,
        center: museum,
    });
    const marker = new google.maps.Marker({
        position: museum,
        map: map,
    });
}