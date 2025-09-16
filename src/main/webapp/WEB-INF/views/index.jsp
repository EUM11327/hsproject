<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>미세먼지 지도(PM10)</title>

<!-- Leaflet CSS/JS  -->
<link rel="stylesheet"
	href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

<style>
/* ===== 공통/네비/상태바 스타일 ===== */
body {
	font-family: Arial, sans-serif;
	margin: 0;
}

h2 {
	margin: 16px 24px 8px;
}

.links {
	display: flex;
	gap: 12px;
	flex-wrap: wrap;
	margin: 8px 0 24px;
}

.metric-link {
	display: inline-block;
	padding: 8px 12px;
	border-radius: 8px;
	border: 1px solid #ddd;
	text-decoration: none;
	color: #333;
}

.metric-link:hover {
	background: #f5f7ff;
}

#statusBar {
	position: fixed;
	left: 0;
	right: 0;
	bottom: 0;
	padding: 10px 16px;
	background: #111;
	color: #fff;
	font-size: 14px;
	z-index: 1001;
}

#statusBar .dot {
	display: inline-block;
	width: 8px;
	height: 8px;
	border-radius: 50%;
	margin: 0 8px 1px 4px;
	vertical-align: middle;
}

.pm10 {
	background: #3692eb;
}

.pm25 {
	background: #ff6384;
}

.navbar {
	background: #132c6f;
}

.navbar ul {
	list-style: none;
	margin: 0;
	padding: 0;
	display: flex;
}

.navbar ul li {
	position: relative;
}

.navbar ul li a {
	display: block;
	padding: 14px 20px;
	color: white;
	text-decoration: none;
	font-weight: bold;
	transition: background 0.3s;
}

.navbar ul li:hover>a {
	background: #304b9a;
}

.navbar ul li ul {
	display: none;
	position: absolute;
	top: 100%;
	left: 0;
	background: #132c6f;
	min-width: 180px;
	border-radius: 0 0 6px 6px;
	box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.3);
	z-index: 1000;
}

.navbar ul li ul li a {
	padding: 12px 16px;
	color: white;
	font-weight: normal;
}

.navbar ul li ul li a:hover {
	background: #304b9a;
}

.navbar ul li:hover ul {
	display: block;
}

/* ===== 지도/툴바 스타일 ===== */
.toolbar {
	display: flex;
	gap: 8px;
	align-items: center;
	padding: 10px 12px;
	border-bottom: 1px solid #eee;
	position: sticky;
	top: 0;
	background: #fff;
	z-index: 999;
}

.toolbar strong {
	margin-right: 8px;
}

#map {
	width: 100%;
	height: calc(100vh - 140px);
}
.legend {
	background: white;
	padding: 8px 10px;
	border: 1px solid #ddd;
	border-radius: 8px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, .08);
	line-height: 1.4;
	font-size: 12px;
}

.legend i {
	display: inline-block;
	width: 12px;
	height: 12px;
	margin-right: 6px;
	vertical-align: -2px;
	border-radius: 2px;
}
</style>
</head>
<body>

	<h2>미세먼지 지도 & 지표 선택</h2>

	<!-- 네비게이션 -->
	<div class="navbar">
		<ul>
			<li><a href="#">미세먼지 농도(pm10)▼</a>
				<ul>
					<li><a class="metric-link" data-metric="PM10" data-sido="서울"
						href="<c:url value='/hs/service/trendchart/pm10'><c:param name='sido' value='서울'/></c:url>">서울</a></li>
					<li><a class="metric-link" data-metric="PM10" data-sido="경기"
						href="<c:url value='/hs/service/trendchart/pm10'><c:param name='sido' value='경기'/></c:url>">경기</a></li>
					<li><a class="metric-link" data-metric="PM10" data-sido="대전"
						href="<c:url value='/hs/service/trendchart/pm10'><c:param name='sido' value='대전'/></c:url>">대전</a></li>
					<li><a class="metric-link" data-metric="PM10" data-sido="부산"
						href="<c:url value='/hs/service/trendchart/pm10'><c:param name='sido' value='부산'/></c:url>">부산</a></li>
				</ul></li>

			<li><a href="#">미세먼지 농도(pm25)▼</a>
				<ul>
					<li><a class="metric-link" data-metric="PM25" data-sido="서울"
						href="<c:url value='/hs/service/trendchart/pm25'><c:param name='sido' value='서울'/></c:url>">서울</a></li>
					<li><a class="metric-link" data-metric="PM25" data-sido="경기"
						href="<c:url value='/hs/service/trendchart/pm25'><c:param name='sido' value='경기'/></c:url>">경기</a></li>
					<li><a class="metric-link" data-metric="PM25" data-sido="대전"
						href="<c:url value='/hs/service/trendchart/pm25'><c:param name='sido' value='대전'/></c:url>">대전</a></li>
					<li><a class="metric-link" data-metric="PM25" data-sido="부산"
						href="<c:url value='/hs/service/trendchart/pm25'><c:param name='sido' value='부산'/></c:url>">부산</a></li>
				</ul></li>
		</ul>
	</div>

	<!-- 지도 상단 툴바 -->
	<div class="toolbar">
		<strong>PM10 지도</strong> <select id="sido">
			<option>서울</option>
			<option>경기</option>
			<option>대전</option>
			<option>부산</option>
		</select>
		<button id="reloadBtn">조회</button>
		<span id="info" style="margin-left: auto; color: #666;"></span>
	</div>

	<!-- 지도 -->
	<div id="map"></div>

	<!-- 하단 상태바 -->
	<div id="statusBar">선택: 없음</div>

	<script>
// ===== 네비 hover 상태바 =====
const statusBar = document.getElementById('statusBar');
function setStatus(metric, sido) {
  const dotClass = metric === 'PM10' ? 'pm10' : 'pm25';
  statusBar.innerHTML = '선택:<span class="dot ' + dotClass + '"></span>' + metric + ' · ' + sido;
}
document.querySelectorAll('.metric-link').forEach(function(a){
  a.addEventListener('mouseenter', function(){ setStatus(a.dataset.metric, a.dataset.sido); });
  a.addEventListener('focus', function(){ setStatus(a.dataset.metric, a.dataset.sido); });
  a.addEventListener('click', function(){ setStatus(a.dataset.metric, a.dataset.sido); });
});

// ===== 지도 초기화/렌더 =====
const map = L.map('map').setView([37.5665, 126.9780], 10);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution:'&copy; OpenStreetMap' }).addTo(map);

const layerGroup = L.layerGroup().addTo(map);

function colorByPM10(v){
  if (v == null || isNaN(v)) return '#9e9e9e';
  const n = Number(v);
  if (n <= 30) return '#2e7d32';
  if (n <= 80) return '#1976d2';
  if (n <= 150) return '#ef6c00';
  return '#d32f2f';
}
function circleStyle(v){
  return { radius: 10, fillColor: colorByPM10(v), color: '#333', weight: 1, opacity: 0.7, fillOpacity: 0.8 };
}

function renderMarkers(items){
  layerGroup.clearLayers();
  const points = [];
  for (var i=0; i<items.length; i++){
    var it = items[i];
    var lat = (it.lat ?? it.dmX);
    var lng = (it.lng ?? it.dmY);
    var pm10 = (it.pm10Value ?? it.pm10 ?? null);
    if (lat == null || lng == null) continue;

    var marker = L.circleMarker([lat, lng], circleStyle(pm10));
    var name = (it.stationName ?? it.name ?? '-');
    var time = (it.dataTime ?? '-');
    var grade = (it.pm10Grade ?? it.grade ?? '-');

    var html =
      '<div style="min-width:180px">' +
        '<div><strong>' + name + '</strong></div>' +
        '<div>PM10: <b>' + (pm10 ?? 'NA') + '</b></div>' +
        '<div>등급: ' + grade + '</div>' +
        '<div>시각: ' + time + '</div>' +
      '</div>';

    marker.bindPopup(html);
    marker.addTo(layerGroup);
    points.push([lat, lng]);
  }
  if (points.length){
    const bounds = L.latLngBounds(points);
    map.fitBounds(bounds.pad(0.2));
  }
  document.getElementById('info').textContent = '표시된 측정소: ' + items.length + '개';
}

async function loadRealtime(){
  const sido = document.getElementById('sido').value;
  document.getElementById('info').textContent = '불러오는 중...';
  try {
    const res = await fetch('/hs/air/realtime?sido=' + encodeURIComponent(sido) + '&metric=PM10');
    if (!res.ok) throw new Error('HTTP ' + res.status);
    const data = await res.json();
    renderMarkers(data);
  } catch (e){
    console.error(e);
    document.getElementById('info').textContent = '로드 실패';
    layerGroup.clearLayers();
  }
}

document.getElementById('reloadBtn').addEventListener('click', loadRealtime);
window.addEventListener('load', loadRealtime);

// 범례
const legend = L.control({position:'bottomright'});
legend.onAdd = function(){
  const div = L.DomUtil.create('div','legend');
  const ranges = [
    {c:'#2e7d32', t:'좋음 (≤30)'},
    {c:'#1976d2', t:'보통 (31~80)'},
    {c:'#ef6c00', t:'나쁨 (81~150)'},
    {c:'#d32f2f', t:'매우나쁨 (151+)'}
  ];
  var html = '<b>PM10</b><br/>';
  for (var i=0; i<ranges.length; i++){
    var r = ranges[i];
    html += '<div><i style="background:' + r.c + '"></i>' + r.t + '</div>';
  }
  div.innerHTML = html;
  return div;
};
legend.addTo(map);
</script>

</body>
</html>
