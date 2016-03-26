function DelayedObjectSelection(scene, callback, timeout) {
	objectSelector = new ObjectSelector();
	var timer;
	scene.addEventListener('focusacquired', function(e) {
		timer = setTimeout(callback, timeout);
	});
	scene.addEventListener('focuslost', function(e) {
		clearTimeout(timer);
	});
}