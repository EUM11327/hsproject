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

/* 네비게이션 */
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
	background: #aaa;
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
	background: #aaa;
}

.navbar ul li:hover ul {
	display: block;
}

/* footer */
.footer {
	background: #132c6f;
	color: #ddd;
	text-align: center;
	padding: 10px 10px;
	font-size: 14px;
}

.footer a {
	color: #ddd;
	text-decoration: none;
	margin: 0 5px;
	transition: color 0.3s;
}

.footer a:hover {
	color: #fff;
}
</style>
</head>
<body>
	<h2>미세먼지 지표 선택</h2>
	<h1></h1>

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
