function moveCameraBetweenNodes(startPosition, finalPosition){
    createjs.Tween.get(startPosition).to(finalPosition, 1000).call(handleComplete);
    // function handleComplete() {
    //     //Tween complete
    // }
}