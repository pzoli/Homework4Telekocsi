currentDialogWidget = "";

function handleComplete(xhr, status, args) {
	if (status == "success") {
		if (args.validationFailed) {
			return false;
		} else {
			PF(currentDialogWidget).hide();
			return true;
		}
	}
}
