<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>미세먼지 지표 선택</title>
<style>
body {
	font-family: Arial, sans-serif;
	padding: 24px;
}

h2 {
	margin-top: 32px;
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
</style>
</head>
<body>

	<h1>미세먼지 지표 선택</h1>

	<h2>미세먼지 농도(pm10)</h2>
	<div class="links">
		<a class="metric-link" data-metric="PM10" data-sido="서울"
			href="<c:url value='/hs/service/trendchart/pm10'><c:param name='sido' value='서울'/></c:url>">서울</a>

		<a class="metric-link" data-metric="PM10" data-sido="경기"
			href="<c:url value='/hs/service/trendchart/pm10'><c:param name='sido' value='경기'/></c:url>">경기</a>

		<a class="metric-link" data-metric="PM10" data-sido="대전"
			href="<c:url value='/hs/service/trendchart/pm10'><c:param name='sido' value='대전'/></c:url>">대전</a>

		<a class="metric-link" data-metric="PM10" data-sido="부산"
			href="<c:url value='/hs/service/trendchart/pm10'><c:param name='sido' value='부산'/></c:url>">부산</a>
	</div>

	<h2>초 미세먼지 농도(PM25)</h2>
	<div class="links">
		<a class="metric-link" data-metric="PM25" data-sido="서울"
			href="<c:url value='/hs/service/trendchart/pm25'><c:param name='sido' value='서울'/></c:url>">서울</a>

		<a class="metric-link" data-metric="PM25" data-sido="경기"
			href="<c:url value='/hs/service/trendchart/pm25'><c:param name='sido' value='경기'/></c:url>">경기</a>

		<a class="metric-link" data-metric="PM25" data-sido="대전"
			href="<c:url value='/hs/service/trendchart/pm25'><c:param name='sido' value='대전'/></c:url>">대전</a>

		<a class="metric-link" data-metric="PM25" data-sido="부산"
			href="<c:url value='/hs/service/trendchart/pm25'><c:param name='sido' value='부산'/></c:url>">부산</a>
	</div>

	<div id="statusBar">선택: 없음</div>

	<script>
  const statusBar = document.getElementById('statusBar');

  function setStatus(metric, sido) {
    const dotClass = metric === 'PM10' ? 'pm10' : 'pm25';
    statusBar.innerHTML = `선택:<span class="dot ${dotClass}"></span>${metric} · ${sido}`;
  }

  document.querySelectorAll('.metric-link').forEach(a => {
    a.addEventListener('mouseenter', () => setStatus(a.dataset.metric, a.dataset.sido));
    a.addEventListener('focus', () => setStatus(a.dataset.metric, a.dataset.sido));
  });

  document.querySelectorAll('.metric-link').forEach(a => {
    a.addEventListener('click', (e) => {
      setStatus(a.dataset.metric, a.dataset.sido);
      // e.preventDefault();
      // setTimeout(() => window.location.href = a.href, 80);
    });
  });
</script>

</body>
</html>
