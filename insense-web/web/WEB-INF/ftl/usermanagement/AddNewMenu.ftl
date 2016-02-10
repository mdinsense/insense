<html>
<script type="text/javascript">
	function validate() {
		if ($("#menuName").val() == "") {
			alert("Menu name should not be blank");
			return false;
		}
		document.getElementById("AddMenuForm").setAttribute("action","<@spring.url '/AddMenu.ftl' />");
		document.getElementById("AddMenuForm").setAttribute("method", "POST");
		document.getElementById("AddMenuForm").submit();
	}
</script>
</head>
<body>
	<section class="drop mtlg">
		<div class="cm">
			<div class="hd">
				<h3>Add New Menu</h3>
			</div>
			<form modelAttribute="ApplicationForm" action="ApplicationConfig.ftl"
				name="AddMenuForm" id="AddMenuForm" target="_top" method="POST">
				<div class="bd">
					<div class="lblFieldPair">
						<div class="lbl">
							<label for="menuName">Menu Name</label>
						</div>
						<div class="input">
							<input id="menuName" name="menuName" type="text">
						</div>
					</div>
					<div class="lblFieldPair">
						<div class="lbl">
							<label for="actionUrl">Menu Action</label>
						</div>
						<div class="input">
							<input id="actionUrl" name="actionUrl" type="text">
						</div>
					</div>
					<div class="lblFieldPair">
						<div class="lbl">
							<label for="parentMenuId">Parent Menu</label>
						</div>
						<div class="input">
							<select id="parentMenuId" name="parentMenuId"
								onChange="checkMenus()">
								<option value="0">----------------Select--------------</option>
								<#list Session.menuList! as menu>
									<#if menu.menu.menuId != -1>
										<option value="${menu.menu.menuId!}">${menu.menu.menuName!}</option>
									</#if>	
								</#list>
							</select>
						</div>
					</div>
				</div>
			</form>
			<div class="btnBar">
				<a href="#" onClick="validate()" class="btn"><span>Submit</span></a>
			</div>
		</div>
	</section>
</body>
</html>