<script>
function deleteAlert() {
	var selected = 0;
	$(':checkbox').each(function() {
		if (this.checked == true) {
			selected = 1;
		}

	});
	if (selected == 1) {
		if (confirm("All existing data will be deleted from DB. Click OK if you want to proceed else click cancel. ")) {
			return true;
		} else {
			return false;
		}
	} else {
		alert('Please Select at least one Service to Update');
		return false;
	}

}

function validateInputValues() {
	if (deleteAlert() ){
		document.getElementById("wserviceUpdateForm").setAttribute('action', "<@spring.url '/ReloadWebservices.ftl' />");
		document.getElementById("wserviceUpdateForm").setAttribute('method', "post");
		document.getElementById("wserviceUpdateForm").submit();
	}
	return false;
}

$(function() {
	$('#select_all').click(function(event) {
		if (this.checked) {
			// Iterate each checkbox
			$(':checkbox').each(function() {
				this.checked = true;
			});
		}
		if (!this.checked) {
			// Iterate each checkbox
			$(':checkbox').each(function() {
				this.checked = false;
			});
		}
	});
});

</script>

<body onLoad="window.scrollTo(0,readCookie('ypos'))">
<#if success?exists>
<div class="infoModule visible" id="tcId1">
		<div class="messageBodyContent"><span class="icon" title="Information"></span>${success}</div>
</div>
</#if>
<section class="drop mtlg">
		<div class="cm">
			<table cellpadding='0' cellspacing='0' width="100%;">
				<tr>
					<td class="hd">
						<h3>Update Web Services</h3>
					</td>
				</tr>
				<tr class="bd">
					<td align="center">
						<form style='width: 800px' method="POST" id="wserviceUpdateForm"
							name="wserviceUpdateForm" action='<@spring.url "/WSUpdate.ftl"/>'
							onSubmit="return deleteAlert()">
							<input type="hidden" value="UpdateServiceDetails" name="method" id="method"/>
							<div class="Datatable" width="100%">
								<table class="bd rowheight35">
									<br />
									<tr bgcolor="#EAEBED">
										<td class="hd"><b>Service</b></td>
										<td class="hd"><b>Updated Date</b></td>
										<td class="hd"><b>Updated Time</b></td>
										<td class="hd"><input type="checkbox" name="select-all"
											id="select_all" onclick="selectAll(this)"></td>
									</tr>
									<#list wservicesList as service>
									<tr>
										<td>
											<div id="service" name="service">
												${service.serviceName!}</div>
										</td>

										<td>
											<div id="updatedDate" name="updatedDate">
												${service.modifiedDate!}</div>
										</td>

										<td>
											<div id="updatedTime" name="updatedTime">
												${service.modifiedTime!}</div>
										</td>
										<td>
											<div id="checkboxColumn" name="checkboxColumn">
												<input type="checkbox" name="${service.serviceName!}"
													id="checkbox" />
											</div>
										</td>
									</tr>
									</#list>
								</table>
							</div>
						</form>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr class="bd">
					<td class="btnBar"><a href="#" onClick="validateInputValues()"
						id="updateServiceDetails1" name="method1" class="btn"><span>Update
								Service Details</span></a></td>
				</tr>
			</table>
		</div>
	</section>


</body>