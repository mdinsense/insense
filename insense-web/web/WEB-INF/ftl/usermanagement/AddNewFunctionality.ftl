<html>
<head>
<script type="text/javascript">

function getFunctioanlityList() {
	var functionalityId = document.functionalityForm.pageName.options[document.functionalityForm.pageName.selectedIndex].value;
	document.getElementById("functionalityName").value="";
	$('#functionalityTable').empty();
	if(functionalityId > 0){
		loadFunctionalityDropdown(functionalityId,"functionalityTable");
	}
}

function validateAndSave()
{
	var functionalityName=document.functionalityForm.functionalityName.value;
	
	if( $('#pageName').val() == -1) {
		alert('Please Select Page');
		return false;
	}
	if(functionalityName==""){
		alert('Application Name should not be blank');
		document.functionalityForm.functionalityName.focus();
		return false;
	}else if(!specialCharValidate(functionalityName)){
		alert('Special Characters are not allowed');
		document.functionalityForm.functionalityName.focus();
		return false;
	}else if(!textAlphanumeric(functionalityName)){
		alert('Application Name is not alphanumeric');
		document.functionalityForm.functionalityName.focus();
		return false;
	}	
	
	document.getElementById("addFunctionalityForm").action="<@spring.url "/AddFunctionality.ftl" />";
	document.getElementById("addFunctionalityForm").submit();
}
</script>
</head>
<body>
 <section class="drop mtlg">
<div class="cm">
		<div class="hd">
			  <h3>Add New Functionality</h3>
		</div>	
<form style='width:800px' method="POST" name="functionalityForm" action='AddFunctionality.ftl' id= "addFunctionalityForm">
<input type="hidden" id="functionalityId" name="functionalityId"  value="" >
<div class="bd">
		<div class="lblFieldPair">
				<div class="lbl">
				  <label for="pageName">Choose Page</label>
				</div>
				<div class="input">
				  <select id="pageName" name="pageName" onChange="getFunctioanlityList()" tabindex="2"> 
						<option value="-1">----------------Select--------------</option>
						<#list subMenuList! as pagename> 
								<option value="${pagename.menuId!}">	${pagename.menuName!}</option>	 
						</#list>
				  </select>
				</div>
		</div>
		<div class="lblFieldPair">
				<div class="lbl">
				  <label for="functionalityName">Functionality</label>
				</div>
				<div class="input">
				  <input id="functionalityName" name="functionalityName" type="text" id="functionalityName" >
				</div>
		</div>
</div>

</form>
<div class="btnBar"> <a href="#"  onClick="validateAndSave()" class="btn"><span>Submit</span></a></div>
</div>
</section>

<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="Add_Functionality_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
			<table id="functionalityTable" class="styleA fullwidth sfhtTable" summary="">
			</table>
		</div>
	</div>
</div>
</body>
</html>