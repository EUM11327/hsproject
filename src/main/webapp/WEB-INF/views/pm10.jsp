<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
	<h2>서울 PM10 시간별 추이</h2>
	<div id="chartContainer">
		<canvas id="pm10Chart"></canvas>
	</div>

	<script>
        // JSP에서 EL로 DTO 리스트를 JS 배열로 변환
        const labels = [
            <c:forEach var="row" items="${pm10TrendList}" varStatus="status">
                '${row.dataTime}'<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        const dataValues = [
            <c:forEach var="row" items="${pm10TrendList}" varStatus="status">
                ${row.pm10Avg}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        const ctx = document.getElementById('pm10Chart').getContext('2d');

        const pm10Chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'PM10 농도 (㎍/㎥)',
                    data: dataValues,
                    borderColor: 'rgba(54, 162, 235, 1)',
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    fill: true,
                    tension: 0.4, // 곡선 형태
                    pointBackgroundColor: 'rgba(54, 162, 235, 1)',
                    pointRadius: 5
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top'
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false
                    }
                },
                interaction: {
                    mode: 'nearest',
                    axis: 'x',
                    intersect: false
                },
                scales: {
                    x: {
                        display: true,
                        title: {
                            display: true,
                            text: '시간'
                        }
                    },
                    y: {
                        display: true,
                        title: {
                            display: true,
                            text: 'PM10 (㎍/㎥)'
                        },
                        beginAtZero: true
                    }
                }
            }
        });
    </script>

</body>
</html>