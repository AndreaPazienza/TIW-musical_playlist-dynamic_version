<!DOCTYPE html>
<html>
	<!-- Head of the page -->
	<head>
	    <meta charset="UTF-8">
	    <link rel="stylesheet" href="resources/style/bootstrap_4.4.1.min.css">
	    <link rel="stylesheet" href="resources/style/order.css">
	    <script src="resources/script/home.js" defer></script>
	    <script src="resources/script/order.js"></script>
	    <script src="resources/script/utils.js" defer></script>
		<title>MainPage</title>
	</head>
	<!-- Body of the page -->
	<body>
		<div class="container">
			<jsp:include page="header.jsp"/>
			<!--  Elements of the home page -->
			<div id="homePageID">
				<!-- Playlist list  -->
				<div class="row my-2 p-1 bg-light border border-secondary rounded">
					<div class="col-sm-6">
						<div>
							<h1>Playlist collection</h1><hr>
							<div id="playlistContainerID"></div>
						</div>
					</div>
				</div>
				<!-- Order playlist list -->
				<div id=modifyPlaylistOrderID class="row my-2 p-1 bg-light border border-secondary rounded">
					<div class="col-sm-6">
						<div id=modifyPlaylistOrderContainerID class="list-section">
							<h2 id=modifyPlaylistOrderNameID>Order playlist</h2><hr>
							<div>
								<ul id=modifyPlaylistOrderListID class="sortable-list" ondragover="allowDrop(event)"></ul>
							</div>
						</div>
					</div>
				</div>
				<!-- Create new playlist form -->
				<div class="row my-2 p-1 bg-light border border-secondary rounded">
					<div class="col-sm-6">
						<div>
							<h2>Create new playlist</h2><hr>
							<form id="createPlaylistFormID">
								<div class="row py-1">
									<input class="col-sm-6" type="text" id="playlistName" placeholder="Playlist name" name="playlistName" required>
								</div>
								<div class="row py-1">
									<select class="col-sm-6" id="allSongSelectID" name="selectedSongs" multiple="multiple" required></select> 
								</div>
								<div class="row py-1">
									<input class="btn btn-primary btn-sm col-sm-4 m-3" type="button" name="CreatePlaylist" value="CREATE PLAYLIST">
								</div>
							</form>
						</div>
					</div>
				</div>
				<!--  Add new song form -->
				<div class="row my-2 p-1 bg-light border border-secondary rounded">
					<div class="col-sm-6">
						<div>
							<h2>Add new song</h2><hr>
							<form id=addSongFormID>
								<div class="row py-1">
									<label class="col-sm-4 form-label" for="songTitle">Song title:</label>
									<input class="col-sm-6 form-control" type="text" id="songTitle" name="songTitle" required>
								</div>
								<div class="row py-1">
									<label class="col-sm-4 form-label" for="albumTitle">Album title:</label>
									<input class="col-sm-6 form-control" type="text" id="albumTitle" name="albumTitle" required>
								</div>
								<div class="row py-1">
									<label class="col-sm-4 form-label" for="imageFile">Image file:</label>
									<input class="col-sm-6 form-control" type="file" id="imageFile" name="imageFile" accept="image/*" required>
								</div>
								<div class="row py-1">
									<label class="col-sm-4 form-label" for="artist">Artist:</label>
									<input class="col-sm-6 form-control" type="text" id="artist" name="artist" required>
								</div>
								<div class="row py-1">
									<label class="col-sm-4 form-label" for="publicationYear">Publication year:</label>
									<input class="col-sm-6 form-control" type="number" id="publicationYear" name="publicationYear" required>
								</div>
								<div class="row py-1">
									<label class="col-sm-4 form-label" for="musicalGenre">Musical genre:</label>
									<select class="col-sm-6 form-control form-control-sm selectOdm" id="musicalGenre" name="musicalGenre" required>
										<option value="Rap">Rap</option>
										<option value="Rock">Rock</option>
										<option value="Pop">Pop</option>
										<option value="Jazz">Jazz</option>
										<option value="Classic">Classic</option>
									</select>
								</div>
								<div class="row py-1">
									<label class="col-sm-4 form-label" for="audioFile">Audio file:</label> 
									<input class="col-sm-6 form-control" type="file" id="audioFile" name="audioFile" accept="audio/*" required> 
								</div>
								<div class="row py-1">
									<input class="btn btn-primary btn-sm col-sm-4 m-3" type="button" name="AddSong"	value="ADD SONG">
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<!-- Elements of the playlist page -->
			<div id ="playlistPageID">
				<!-- Back to the home page -->
				<button class="btn btn-primary btn-sm col-sm-2 m-3" id="backToHomeID">HOME</button><hr>
				<!-- Playlist song table -->
				<div class="row my-2 p-1 bg-light border border-secondary rounded">
					<div class="col-sm-12">
						<div class="row">
						    <h1 id="playlistSongTableTitleID"></h1>
						    <table id="playlistSongTableID" class="table">
						        <tr id="playlistSongTableRowID"></tr>
						    </table>
						</div>
						<div class="row">
							<!-- Previous page button -->
							<div class="col-sm-4">
								<button class="btn btn-primary btn-sm m-3" id="previousPageID">PREVIOUS</button>
						    </div>
						    <!-- Next page button -->
						    <div class="col-sm-4 offset-sm-4">
						    	<button class="btn btn-primary btn-sm m-3 " id="nextPageID">NEXT</button>
							</div>
						</div>
					</div>
				</div>
				<!-- Add song to playlist form -->
				<div class="row my-2 p-1 bg-light border border-secondary rounded">
					<div class="col-sm-6">
						<div>
					        <h2 id="addPlaylistSongFormTitleID"></h2><hr>
					        <form id="addPlaylistSongFormID">
					        	<input type="hidden" id="hiddenPlaylistID" name="playlistID" required>
								<div class="row py-1">
						            <select class="col-sm-6" id="playlistSongSelectID" name="selectedSongs" multiple="multiple" required></select>
								</div>
					            <input class="btn btn-primary btn-sm col-sm-4 m-3" type="button" name="AddToPlaylist" value="ADD TO PLAYLIST">
					        </form>
					    </div>
					</div>
				</div>
			</div>
			<!-- Elements of the player page -->
			<div id ="playerPageID"> 
				<button class="btn btn-primary btn-sm col-sm-2 m-3" id="backToPlaylistID">PLAYLIST</button><hr>
				<!-- Song details -->
			    <h1 id="playerSongTitle"></h1>
			    <img id="playerImage" width="300" src="" alt="Song Image"/>
			    <p>
			        <strong>Album: </strong><span id="playerAlbumTitle"></span><br>
			        <strong>Artist: </strong><span id="playerArtist"></span><br>
			        <strong>Year: </strong><span id="playerPublicationYear"></span><br>
			        <strong>Genre: </strong><span id="playerMusicalGenre"></span><br>
			    </p>
				<!-- Audio player -->
			    <audio id="audioPlayer" controls>
			        <source id="audioSource" src="" type="audio/mpeg">
			    </audio>
			</div> 
		</div>
	</body>
</html>
