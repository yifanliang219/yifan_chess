var gameState;
var possibleMoves;
var possibleStartSquares = new Set();
var selectedId;
var possibleDesOfSelectedId = new Set();
var processingClick = false;
var moveType;
var status;

buildBoard();

function buildBoard() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			gameState = JSON.parse(this.responseText);
			var board = gameState.board;
			possibleMoves = gameState.possibleMovesByActivePlayer;
			computePossibleStartSquares();
			document.getElementById("status").innerHTML = gameState.active_player + "'s turn" + "<br>";
			document.getElementById("moveSummary").innerHTML = gameState.possibleMovesSummary;
			var chessTable = document.getElementById("chessTable");
			for (var rank = 8; rank >= 1; rank--) {
				var row = document.createElement("tr");
				chessTable.appendChild(row);
				for (var file = 1; file <= 8; file++) {
					var data = document.createElement("td");
					row.appendChild(data);
					var image = document.createElement("img");
					image.src = getImage(board, file, rank);
					image.id = file + "," + rank;
					image.addEventListener("click", function() { processClick(this.id) });
					data.appendChild(image);
				}
			}

			var promotionalPieceTable = document.getElementById("promotionPieces");
			var headerRow = document.createElement("th");
			var promotionPieces = ["ROOK", "KNIGHT", "BISHOP", "QUEEN"];
			headerRow.innerHtml = "Promotion Pieces"
			promotionalPieceTable.appendChild(headerRow);
			for (var i = 0; i < 4; i++) {
				var row = document.createElement("tr");
				var data = document.createElement("td");
				row.appendChild(data);
				var image = document.createElement("img");
				image.src = "img/WHITE" + promotionPieces[i] + "_DARK.png";
				image.piece = promotionPieces[i];
				image.addEventListener("click", function() { promotePiece(this.piece) });
				data.appendChild(image);
				promotionalPieceTable.appendChild(row);
			}

		}
	};
	xmlhttp.open("GET", "board.json", true);
	xmlhttp.send();
}

function computePossibleStartSquares() {
	possibleStartSquares = new Set();
	for (var i = 0; i < possibleMoves.length; i++) {
		var start_file = possibleMoves[i].start_file;
		var start_rank = possibleMoves[i].start_rank;
		var start_sq = start_file + "," + start_rank;
		possibleStartSquares.add(start_sq);
	}
}

function searchMoveType(selectedId, id) {
	var start_file = selectedId.replace(",", "").charAt(0);
	var start_rank = selectedId.replace(",", "").charAt(1);
	var end_file = id.replace(",", "").charAt(0);
	var end_rank = id.replace(",", "").charAt(1);
	for (m of possibleMoves) {
		if (m.start_file == start_file && m.start_rank == start_rank && m.end_file == end_file && m.end_rank == end_rank) {
			return m.moveType;
		}
	}
}

function updateBoard(url) {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			gameState = JSON.parse(this.responseText);
			var board = gameState.board;
			possibleMoves = gameState.possibleMovesByActivePlayer;
			computePossibleStartSquares();
			document.getElementById("status").innerHTML = gameState.active_player + "'s turn" + "<br>";
			document.getElementById("moveSummary").innerHTML = gameState.possibleMovesSummary;
			for (var rank = 8; rank >= 1; rank--) {
				for (var file = 1; file <= 8; file++) {
					var id = file + "," + rank;
					var image = document.getElementById(id);
					image.src = getImage(board, file, rank);
				}
			}
			var message = gameState.message;
			document.getElementById("message").innerHTML = message + "<br>";
			status = gameState.status;
			getAiMove();
			processingClick = false;
		}
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function getAiMove() {
	if (status == "WAIT_PROMOTION") {
		return;
	}
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			gameState = JSON.parse(this.responseText);
			var board = gameState.board;
			possibleMoves = gameState.possibleMovesByActivePlayer;
			computePossibleStartSquares();
			document.getElementById("status").innerHTML = gameState.active_player + "'s turn" + "<br>";
			document.getElementById("moveSummary").innerHTML = gameState.possibleMovesSummary;
			for (var rank = 8; rank >= 1; rank--) {
				for (var file = 1; file <= 8; file++) {
					var id = file + "," + rank;
					var image = document.getElementById(id);
					image.src = getImage(board, file, rank);
				}
			}
			var message = gameState.message;
			document.getElementById("message").innerHTML = message + "<br>";
			status = gameState.status;
			processingClick = false;
		}
	};
	xmlhttp.open("GET", "board.json/ai", true);
	xmlhttp.send();
}

function getImage(board, file, rank) {
	var sq = board.squares[8 - rank][file - 1];
	var piece = sq.piece;
	if (piece.pieceType == "NULL") {
		return "img/" + sq.color + ".png";
	} else {
		return "img/" + piece.owner + piece.pieceType + "_" + sq.color + ".png";
	}

}

function promotePiece(piece) {
	processingClick = true;
	if (status != "WAIT_PROMOTION") {
		return;
	}
	var url = "board.json/promotion?piece=" + piece;
	updateBoard(url);
}

function processClick(id) {
	if (processingClick) return;
	processingClick = true;
	removeAllInvertedImages();
	// if click on a piece that can be moved, highlight the piece and the possible squares to move to
	if (selectedId == null && possibleStartSquares.has(id)) {
		selectedId = id;
		highlightSelectedPieceAndItsMoves();
		processingClick = false;
	}
	// if click on the selected piece again, undo the selection
	else if (selectedId == id) {
		selectedId = null;
		possibleDesOfSelectedId = new Set();
		processingClick = false;
	}
	else {
		if (possibleDesOfSelectedId.has(id)) {
			moveType = searchMoveType(selectedId, id);
			var url = "board.json?start_file=" + selectedId.replace(",", "&start_rank=") + "&end_file=" + id.replace(",", "&end_rank=") + "&moveType=" + moveType;
			updateBoard(url);
			selectedId = null;
			possibleDesOfSelectedId = new Set();
		} else {
			selectedId = null;
			possibleDesOfSelectedId = new Set();
			processingClick = false;
		}
	}
}

function removeAllInvertedImages() {
	var selected = document.getElementsByClassName("inverted");
	for (var i = selected.length - 1; i >= 0; i--) {
		selected[i].classList.remove('inverted');
	}
}

function highlightSelectedPieceAndItsMoves() {
	document.getElementById(selectedId).classList.add('inverted');
	for (var i = 0; i < possibleMoves.length; i++) {
		var start_file = possibleMoves[i].start_file;
		var start_rank = possibleMoves[i].start_rank;
		var start_sq = start_file + "," + start_rank;
		if (start_sq == selectedId) {
			var end_file = possibleMoves[i].end_file;
			var end_rank = possibleMoves[i].end_rank;
			var end_sq = end_file + "," + end_rank;
			possibleDesOfSelectedId.add(end_sq);
			document.getElementById(end_sq).classList.add('inverted');
		}
	}
}

