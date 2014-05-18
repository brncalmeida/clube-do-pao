<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="now" class="java.util.Date" />

<tiles:insertTemplate template="/WEB-INF/jsp/template.jsp">
	<tiles:putAttribute name="body">
		<script>
			$(document).ready(function() {
				var date = new Date();
				var day = date.getDate();
				var month = date.getMonth() + 1;
				var year = date.getFullYear();
				if (month < 10)
					month = "0" + month;
				var today = year + "-" + month + "-" + day;
				$('*[name=data]').val(today);

				$('.sem_pao').css('color', 'red').css('font-weight', 'bold');

			});
		</script>

		<div class="panel panel-default">
			<div class="panel-heading">
				<fmt:message key="gerar.lista" />
			</div>
			<div class="panel-body">
				<form id="cadastro_membros" action="<c:url value="/controle/gerar" />" method="post" class="buttonForm">
					<div class="row  col-mb-4">
						<div class="col-sm-3">
							<div class="input-group">
								<span class="input-group-addon"><fmt:message key="data.inicial" /></span> <input type="date" name="data" class="form-control" required />
							</div>
						</div>
						<div class="col-sm-2">
							<button type="submit" class="btn btn-default" title="<fmt:message key="gerar" />">
								<fmt:message key="gerar" />
							</button>
						</div>
					</div>
				</form>
			</div>
		</div>

		<!-- TODO: sistema de susgestão ainda em implementação -->
		<!--
		<c:if test="${not empty sugestoes}">
			<div class="alert alert-warning">
				<fmt:message key="sugestao.membros.sobrecarregados">
					<fmt:param value="${sugestoes.membrosASeremColocados}" />
					<fmt:param value="${sugestoes.disponibilidadesSobrecarregadas}" />
					<fmt:param value="${sugestoes.membrosASeremRemovidos}" />
				</fmt:message>

			</div>
		</c:if>-->
		<c:if test="${not empty sugestoes.disponibilidadesAusentes}">
			<div class="alert alert-danger">
				<fmt:message key="sugestao.dias.ausentes">
					<fmt:param value="${sugestoes.disponibilidadesAusentes}" />
				</fmt:message>
			</div>
		</c:if>

		<c:if test="${not empty calendario}">
			<div class="panel panel-default print-area">
				<div class="panel-heading">
					<fmt:message key="descricao.table.controle">
						<fmt:param value="${qtdPaes}" />
					</fmt:message>
				</div>
				<table class="table table-striped table-hover table-condensed">
					<thead>
						<tr>
							<th><fmt:message key="data" /></th>
							<th><fmt:message key="dia" /></th>
							<th><fmt:message key="membro" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${calendario}" var="dia">
							<tr>
								<td><fmt:formatDate type="date" value="${dia.key}" /></td>
								<td><fmt:formatDate pattern="E" value="${dia.key}" /></td>
								<c:choose>
									<c:when test="${not empty dia.value}">
										<td>${dia.value}</td>
									</c:when>
									<c:otherwise>
										<td><span class="sem_pao"><fmt:message key="error.sem.pao" /></span></td>
									</c:otherwise>
								</c:choose>

							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="panel-footer remove-print-area">
					<!--  botão de impressão -->
					<a id="botao_impressao"> <span class="glyphicon glyphicon-print btn-lg" title="<fmt:message key="imprimir" />"></span></a>
				</div>
			</div>
			<div class="panel panel-default" id="accordion">
				<div class="panel-heading">
					<fmt:message key="header.sobrecarga.membros" />
				</div>
				<div class="panel-body">
					<ul class="list-group">
						<c:forEach items="${sobrecargas}" var="sobrecarga">
							<c:choose>
								<c:when test="${sobrecarga.key > 1}">
									<li class="list-group-item"><span class="badge">${sobrecarga.key} programações</span> <span>${sobrecarga.value}</span></li>
								</c:when>
								<c:when test="${sobrecarga.key == 1}">
									<li class="list-group-item"><span class="badge">1 programação</span> <span>${sobrecarga.value}</span></li>
								</c:when>
								<c:otherwise>
									<li class="list-group-item"><span class="badge">Sem programação</span> <span>${sobrecarga.value}</span></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</ul>
				</div>
			</div>
		</c:if>


	</tiles:putAttribute>
</tiles:insertTemplate>