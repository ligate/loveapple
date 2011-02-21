<%@ include file="/WEB-INF/tags/include.tag" %>
<%@tag import="cn.loveapple.service.controller.SessionLabel;"%>
<c:set var="sessionKey"><%=SessionLabel.LOVEAPPLE_MEMBER%></c:set>
<c:choose>
	<c:when test="${empty sessionScope[sessionKey]}" ><a href="/member"><fmt:message key="msg.member.login"/></a></c:when>
	<c:otherwise><a href="/member/core/info/${sessionScope[sessionKey]['key']['id']}">${sessionScope[sessionKey]['loginId']}</a></c:otherwise>
</c:choose>