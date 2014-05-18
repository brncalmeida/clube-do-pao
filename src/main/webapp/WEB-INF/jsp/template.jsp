<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${locale}" />
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="author" content="Bruno Almeida - brn.calmeida@gmai.com" />
<meta name="description" content="<fmt:message key="meta.description"/>" />
<meta name="keywords" content="vraptor, web, desenvolvimento, development, java, opensource, hsqldb" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Clube do pão</title>

<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/bootstrap.min.css"/>" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/bootstrap-theme.min.css"/>" />
<script src="<c:url value="/js/jquery-2.1.0.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/js/jquery.PrintArea.js"/>"></script>
<script src="<c:url value="/js/bootstrap.file-input.js"/>"></script>
<script>
	$(document).ready(function() {
		$('input[type=file]').bootstrapFileInput();

		$("#botao_impressao").click(function() {
			var camposImpressao = $('.print-area').clone();
			camposImpressao.find(".remove-print-area").remove();
			camposImpressao.printArea({
				extraCss : '<c:url value="/css/bootstrap.min.css"/>'
			});
		});
	});
</script>
<style>
td,th {
	text-align: center
}

.espaco_lateral {
	margin-left: 2px;
}

@media ( max-width : 480px) {
	.nav-tabs>li {
		float: none;
	}
	.nav-tabs>li>a {
		border: 1px solid #dddddd;
	}
	.nav-tabs>li.active>a,.nav-tabs>li.active>a:hover,.nav-tabs>li.active>a:focus
		{
		background: none;
	}
	.radio-inline+.radio-inline,.checkbox-inline+.checkbox-inline {
		margin-left: 5px;
	}
}
</style>


<!--[if lt IE 7]>
    <script src="http://ie7-js.googlecode.com/svn/version/2.0(beta3)/IE7.js" type="text/javascript"></script>
    <![endif]-->
</head>

<body>
	<c:if test="${not empty param.language}">
		<fmt:setLocale value="${param.language}" scope="session" />
	</c:if>


	<div class="navbar navbar-static-top">
		<div class="navbar-inner">
			<div class="container">
				<div class="page-header" id="header_page">
					<h1>
						<fmt:message key="header.clube.pao" />
					</h1>
				</div>
				<ul class="nav nav-tabs">
					<li class="${pagina_ativa == 'membro' ? 'active' : ''}"><a href="<c:url value="/" />#"><span class="glyphicon glyphicon-cog"></span> <fmt:message
								key="menu.cadastro.membro" /></a></li>
					<li class="${pagina_ativa == 'controle' ? 'active' : ''}"><a href="<c:url value="/controle" />"><span
							class="glyphicon glyphicon-list-alt"></span> <fmt:message key="menu.gerar.controle" /></a></li>
					<li class="${pagina_ativa == 'mapa' ? 'active' : ''}"><a href="<c:url value="/mapa" />"><span class="glyphicon glyphicon-globe"></span>
							<fmt:message key="menu.mapa.site" /></a></li>
				</ul>
			</div>
		</div>
	</div>
	<br />
	<div class="container">

		<c:if test="${not empty errors}">
			<c:forEach items="${errors }" var="error">
				<div class="alert alert-danger">${error.message}</div>
			</c:forEach>
		</c:if>
		<c:if test="${not empty notice}">
			<div class="alert alert-success">${notice }</div>
		</c:if>
		<tiles:insertAttribute name="body" />
	</div>
	<div class="footer-page"></div>
</body>
</html>