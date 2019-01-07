<jsp:useBean id="currentDate" class="java.util.Date" />
<fmt:formatDate var="year" value="${currentDate}" pattern="yyyy" />
<!-- Footer -->
<footer class="py-5 bg-dark">
	<div class="container">
		<p class="m-0 text-center text-white">Copyright &copy; SparkAndBid ${year}</p>
	</div>
	<!-- /.container -->
</footer>