<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PM25 추이 - ${sido}</title>
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

.section {
	width: 80%;
	max-width: 900px;
	margin: 30px auto;
	background: #fff;
	padding: 20px;
	border-radius: 12px;
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.section h3 {
	margin: 0 0 12px;
}
</style>
</head>
<body>
	<h2>PM25 평균 (${sido})</h2>

	<c:choose>
		<c:when test="${empty pm25TrendList}">
			<p style="text-align: center">데이터가 없습니다.</p>
		</c:when>
		<c:otherwise>
			<div class="section">
				<h3>PM2.5 (㎍/㎥)</h3>
				<canvas id="pm25Chart"></canvas>
			</div>

			<script>
    const labels = [
        <c:forEach var="row" items="${pm25TrendList}" varStatus="st">
            '${row.dataTime}'<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];

    const pm25Values = [
        <c:forEach var="row" items="${pm25TrendList}" varStatus="st">
            <c:choose>
                <c:when test="${row.pm25Avg != null}">${row.pm25Avg}</c:when>
                <c:otherwise>null</c:otherwise>
            </c:choose><c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];

    new Chart(document.getElementById('pm25Chart').getContext('2d'), {
        type: 'bar',   
        data: {
            labels: labels,
            datasets: [{
                label: 'PM2.5 (㎍/㎥)',
                data: pm25Values,
                backgroundColor: 'rgba(255, 99, 132, 0.6)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: true, position: 'top' } },
            interaction: { mode: 'nearest', axis: 'x', intersect: false },
            scales: {
                x: { title: { display: true, text: '시간' } },
                y: { title: { display: true, text: 'PM2.5 (㎍/㎥)' }, beginAtZero: true }
            }
        }
    });
</script>

		</c:otherwise>
	</c:choose>
</body>
</html>
