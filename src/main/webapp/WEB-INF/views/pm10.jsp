<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PM10 추이 - ${sido}</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
body {
	font-family: Arial, sans-serif;
	padding: 20px;
	background-color: #f7f7f7;
}

h2 {
	text-align: center;
}

#chartContainer {
	width: 80%;
	max-width: 900px;
	margin: 40px auto;
	background: #fff;
	padding: 20px;
	border-radius: 12px;
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}
</style>
</head>
<body>
	<h2>PM10 평균 (${sido})</h2>

	<c:choose>
		<c:when test="${empty pm10TrendList}">
			<p style="text-align: center">데이터가 없습니다.</p>
		</c:when>
		<c:otherwise>
			<div id="chartContainer">
				<canvas id="pm10Chart"></canvas>
			</div>

			<script>
                const labels = [
                    <c:forEach var="row" items="${pm10TrendList}" varStatus="st">
                        '${row.dataTime}'<c:if test="${!st.last}">,</c:if>
                    </c:forEach>
                ];
                const dataValues = [
                    <c:forEach var="row" items="${pm10TrendList}" varStatus="st">
                        ${row.pm10Avg}<c:if test="${!st.last}">,</c:if>
                    </c:forEach>
                ];

                const ctx = document.getElementById('pm10Chart').getContext('2d');
                new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: 'PM10 (㎍/㎥)',
                            data: dataValues,
                            borderColor: 'rgba(54, 162, 235, 1)',
                            backgroundColor: 'rgba(54, 162, 235, 0.2)',
                            fill: true,
                            tension: 0.4,
                            pointBackgroundColor: 'rgba(54, 162, 235, 1)',
                            pointRadius: 4
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: { display: true, position: 'top' },
                            tooltip: { mode: 'index', intersect: false }
                        },
                        interaction: { mode: 'nearest', axis: 'x', intersect: false },
                        scales: {
                            x: { title: { display: true, text: '시간' } },
                            y: { title: { display: true, text: 'PM10 (㎍/㎥)' }, beginAtZero: true }
                        }
                    }
                });
            </script>
		</c:otherwise>
	</c:choose>
	
	<a href="${pageContext.request.contextPath}/hs/service/index">목록으로</a>
</body>
</html>
