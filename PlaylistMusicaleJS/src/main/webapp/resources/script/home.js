{
	//Elements of the view representing the home page
	let homePage = document.getElementById("homePageID")
	let playlistList = null;
	let modifyPlaylistOrder = null;
	let createPlaylistForm = document.getElementById("createPlaylistFormID");
	let allSongSelectList = null;
	let createPlaylistButton = createPlaylistForm.querySelector("input[type='button']");
	let addSongForm = document.getElementById("addSongFormID");
	let addSongButton = addSongForm.querySelector("input[type='button']");
	//Elements of the view representing the playlist page
	let playlistPage = document.getElementById("playlistPageID");
	let playlistSongTableTitle = document.getElementById("playlistSongTableTitleID");
	let playlistSongTable = null;
	let previousPageButton = document.getElementById("previousPageID");
	let nextPageButton = document.getElementById("nextPageID");
	let addPlaylistSongFormTitle = document.getElementById("addPlaylistSongFormTitleID");
	let addPlaylistSongForm = document.getElementById("addPlaylistSongFormID");
	let playlistSongSelectList = null;
	let addPlaylistSongButton = addPlaylistSongForm.querySelector("input[type='button']");
	let backToHomeButton = document.getElementById("backToHomeID");
	//Elements of the view representing the player page
	let playerPage = document.getElementById("playerPageID")
	let backToPlaylistButton = document.getElementById("backToPlaylistID");
	//Alert container used to display text messages
	let alertContainer = document.getElementById("alertID");

	//Loading of the page and initialization of the graphic elements
  	window.addEventListener("load", function() {
	
		//Adding the event listener on the click of the button to create the playlist
	    createPlaylistButton.addEventListener("click", function(event) {
	      	let form = event.target.closest("form");
	      	if (form.checkValidity()) {
	        	event.preventDefault();
	        	//Servlet call to create the playlist
	        	makeCall("POST", "createPlaylist", form, function(request) { 
	        		if (request.readyState === 4) {
	          			let message = request.responseText;
	          			if (request.status === 200) {
	            			visibleAlert("Playlist created successfully", 0);
	            			playlistList.show();
	          			} else {
	            			visibleAlert(message, 1);
	          			}
	    			}
	      		});
	      	} else {
	        	form.reportValidity();
	      	}
	    });
	
		//Adding the event listener on the click of the button to add a song
	    addSongButton.addEventListener("click", function(event) {
		    let form = event.target.closest("form");
		    if (form.checkValidity()) {
		        event.preventDefault();
		        //Servlet call to add the song
		        makeCall("POST", "addSong", form, function(request) { 
	                if (request.readyState === 4) {
	                    let message = request.responseText;
	                    if (request.status === 200) {
	                        visibleAlert("Song added successfully", 0);
	                        allSongSelectList.show();
	                    } else {
	                        visibleAlert(message, 1);
	                    }
	                }
	            });
	    	} else {
		        form.reportValidity();
		    }
		});
	
		//Adding the event listener on the click of the button to go back in the playlist
		previousPageButton.addEventListener("click", function(event) {
			event.preventDefault();
			playlistSongTable.showPreviousPage();
		});
	
		//Adding the event listener on the click of the button to advance in the playlist
		nextPageButton.addEventListener("click", function(event) {
			event.preventDefault();
			playlistSongTable.showNextPage();		
		});
	
		//Adding the event listener on the click of the button to add songs to a playlist
		addPlaylistSongButton.addEventListener("click", function(event) {
		    let form = event.target.closest("form");
		    if (form.checkValidity()) {
		        event.preventDefault();
		        //Servlet call to add the songs to the playlist
		        makeCall("POST", "addSongToPlaylist", form, function(request) { 
	                if (request.readyState === 4) {
	                    let message = request.responseText;
	                    if (request.status === 200) {
							let playlistID = JSON.parse(request.responseText);
                    		playlistSongTable.show(playlistID, "");
                    		playlistSongSelectList.show(playlistID);
	                        visibleAlert("Playlist updated successfully", 0);
	                    } else {
	                        visibleAlert(message, 1);
						}
	                }
	            });
	    	} else {
		        form.reportValidity();
		    }
		});
		
		//Adding the event listener on the click of the button to go back in the home page
		backToHomeButton.addEventListener("click", function(event) {
			event.preventDefault();
			visibleHomePage();
		});
		
		//Adding the event listener on the click of the button to go back in the playlist page
		backToPlaylistButton.addEventListener("click", function(event) {
			event.preventDefault();
			visiblePlaylistPage();
		});

		//Adding the event listener on the click of the alert section to make it disappear
		alertContainer.addEventListener("click", function() {
			alertContainer.style.display = "none";
		});
		
     	//Initialization of the graphic elements of the page
	    playlistList = new PlaylistList(document.getElementById("playlistContainerID"));
	    modifyPlaylistOrder = new ModifyPlaylistOrder(document.getElementById('modifyPlaylistOrderContainerID'), document.getElementById('modifyPlaylistOrderListID'));
		allSongSelectList = new AllSongSelectList(document.getElementById("allSongSelectID"));
		playlistSongTable = new PlaylistSongTable(document.getElementById('playlistSongTableID'), document.getElementById('playlistSongTableRowID'));
		playlistSongSelectList = new PlaylistSongSelectList(document.getElementById('playlistSongSelectID'));
		
		playlistList.show();
		modifyPlaylistOrder.reset();
		allSongSelectList.show();
		
		visibleHomePage();

    }, false);
    
    //List of playlist of the logged user in the home page
    function PlaylistList(listContainer) {
		
		//Container of the list
		this.listContainer = listContainer;
	    	
	    //Gets and shows the playlist list
		this.show = function() {	
		  	let self = this;
		  	//Servlet call to get the playlist list
		  	makeCall("GET", "getPlaylistList", null, function(request) {
	      		if (request.readyState === 4) {
					let message = request.responseText;
	            	if (request.status === 200) {
	              		let playlistList = JSON.parse(request.responseText);
	              		if (playlistList.length === 0) {
	                		visibleAlert("There are no playlist yet", 0);
	                		return;
	              		}
	              		self.update(playlistList);
            		} else {
              			visibleAlert(message, 1);
            		}
          		}
    		});
		}
		
		//Updates the view of the playlist list
		this.update = function(arrayPlaylist) {		
			let self = this;
			self.listContainer.innerHTML = '';
			//Creation of each item of the menu
	        arrayPlaylist.forEach(function(playlist) {
	            let paragraph = document.createElement('div');
				paragraph.setAttribute("class","parListPlaylist col-sm-4 col-form-label");
	            paragraph.textContent = playlist.playlistName /*- playlist.creationDate*/;
	            paragraph.addEventListener('click', function() {
	               	playlistSongTable.show(playlist.playlistID, playlist.playlistName);
	               	playlistSongSelectList.show(playlist.playlistID);
	               	visiblePlaylistPage();
	            });
	            //Creation of the button to order the playlist
	            let orderPlaylistButton = document.createElement('button');
				orderPlaylistButton.textContent = 'ORDER';
				orderPlaylistButton.setAttribute("class", "btn btn-primary btn-sm");
	            orderPlaylistButton.addEventListener('click', function() {
					modifyPlaylistOrder.show(playlist.playlistID, playlist.playlistName);
	            });
	            let div = document.createElement('div');
	            div.setAttribute("class", "divListPlaylist row py-1");
	            div.appendChild(paragraph);
	            div.appendChild(orderPlaylistButton);
	            self.listContainer.appendChild(div);
	        });	
		}
	}
	
	//List of songs of the playlist to order in the home page
	function ModifyPlaylistOrder(listContainer, draggableList) {
	
		//Container of the list
		this.listContainer = listContainer;
		//List of elements that can be dragged and dropped
	 	this.draggableList = draggableList;
		//List of the songs of the playlist
		this.playlistSongs = [];
		
		//Gets and shows the playlist song list to order
	    this.show = function(playlistID, playlistName) {
	        let self = this;
	        //Servlet call to get the playlist song list to order
	        makeCall("GET", "getPlaylistSongs?playlistID=" + encodeURIComponent(playlistID), null, function(request) {
	            if (request.readyState === 4) {
	                if (request.status === 200) {
	                    let playlistSongs = JSON.parse(request.responseText);
	                    if (playlistSongs.length === 0) {
							self.reset();
	                        visibleAlert("The playlist to order is empty", 0);
	                        return;
	                    } else {
	                        self.playlistSongs = playlistSongs;
	                        self.update(playlistID, playlistName);
	                    }
	                } else {
	                    visibleAlert(request.responseText, 1);
	                }
	            }
	        });
	    }
	    
	    //Updates the view of the list to order
	    this.update = function(playlistID, playlistName) {
			let maxIndex = this.playlistSongs.length - 1;
	        document.getElementById("modifyPlaylistOrderNameID").textContent = "Order " + playlistName;
	        this.draggableList.innerHTML = "";
			//Creation of each item of the list
	        for (let i = 0; i <= maxIndex; i ++) {
	            let song = this.playlistSongs[i];	
			    let listElement = document.createElement("li");
	            listElement.setAttribute ("class", "songLine");
	            //Setting the drag and drop functionality
	            listElement.setAttribute ("draggable", "true");
	            listElement.setAttribute ("ondragstart", "dragStart(event)");
	            listElement.setAttribute ("ondragover", "dragOver(event)");
	            listElement.setAttribute ("ondragend", "dragEnd()");
				listElement.classList.add("songItem");
				listElement.setAttribute ("id", song.songID);
			    listElement.innerText = song.songTitle + " - " + song.albumTitle + " - " + song.artist + " - " + song.publicationYear;
			    this.draggableList.appendChild(listElement);
	        }
	        
			//Adding the event listener on the click of the button to confirm the order
			let confirmOrderButton = document.createElement('button');
			confirmOrderButton.textContent = 'CONFIRM';
			confirmOrderButton.setAttribute("class","btn btn-primary btn-sm col-sm-4 my-3");
			confirmOrderButton.addEventListener('click', function() {
				
			  	var modifyPlaylistOrderForm = document.createElement("form");
				var songOrderedArray = [];
				var songOrderedList = document.getElementsByClassName("songItem");
				for (var i = 0; i < songOrderedList.length; i++) {
					songOrderedArray[i] = songOrderedList.item(i).getAttribute("id");
				}
				
				//Creation of the json file
				var json = {
					"playlistID": playlistID,
					"orderedPlaylist": songOrderedArray
				}  
			
				//Sending the json as a hidden input of a form
				var hiddenInput = document.createElement("input");
				hiddenInput.type = "text";
				hiddenInput.name = "playlistOrder";
				hiddenInput.value = JSON.stringify(json);
				modifyPlaylistOrderForm.appendChild(hiddenInput);
				//Servlet call to modify the order to the playlist
			  	makeCall("POST", "modifyPlaylistOrder", modifyPlaylistOrderForm, function(request) { 
		    		if (request.readyState === 4) {
		      			let message = request.responseText;
		      			if (request.status === 200) {
		        			visibleAlert("Playlist " + playlistName + " ordered", 0);
		        			modifyPlaylistOrder.reset();
		      			} else {
		        			visibleAlert(message, 1);
		      			}
					}
  				});
      		});
	      
	  	this.draggableList.appendChild(confirmOrderButton);
	  	this.listContainer.style.display = 'block';

	    }
	    
	    //Reset the view of the list
	    this.reset = function() {
		    while (this.draggableList.firstChild) {
		        this.draggableList.removeChild(this.draggableList.firstChild);
		    }
		    this.playlistSongs = [];
		    this.currentIndex = 0;
		    this.lastIndex = 0;
		    this.listContainer.style.display = 'none';
		} 
	}
	
	//List of the song that make up the drop-down menu of the home page
 	function AllSongSelectList(listContainer) {
		
		//Container of the list
		this.listContainer = listContainer;
	    	
		//Gets the song list and shows the drop down menu
		this.show = function() {
		  	let self = this;
		  	//Servlet call to get the song list
		  	makeCall("GET", "getAllSongs", null, function(request) {
	      		if (request.readyState === 4) {
	            	if (request.status === 200) {
	              		let songList = JSON.parse(request.responseText);
	              		if (songList.length === 0) {
	                		visibleAlert("There are no songs saved", 0);
	                		return;
	              		}
	              		self.update(songList);
            		} else {
              			visibleAlert(request.responseText, 1);
            		}
          		}
    		});
		}
		
		//Updates the view of the drop down menu
		this.update = function(arraySong) {
			let self = this;
			self.listContainer.innerHTML = '';
			//Creation of each item of the menù
	        arraySong.forEach(function(song) {
			    let option = document.createElement('option');
			    option.value = song.songID;
			    option.textContent = song.songTitle + " - " + song.albumTitle + " - " + song.artist + " - " + song.publicationYear;
			    self.listContainer.appendChild(option);
			});		
		}
	}
	
	//Table of songs of the playlist of the playlist page
	function PlaylistSongTable(tableContainer, tableRow) {
		
		//Container of the table
 		this.tableContainer = tableContainer;
 		//Container of the only row of the table
    	this.tableRow = tableRow;
    	//List of the songs of the playlist
    	this.playlistSongs = [];
    	//Index of the first song displayed on the view
    	this.currentIndex = 0;
    	//index of the last song in the list
		this.lastIndex = 0;
		
		//Gets the playlist song list and shows the table
	    this.show = function(playlistID, playlistName) {
			if (playlistName != "") {
				playlistSongTableTitle.textContent = playlistName;
				addPlaylistSongFormTitle.textContent = "Add song to " + playlistName;
				
			}
	        let self = this;
	        //Servlet call to get the playlist song list
	        makeCall("GET", "getPlaylistSongs?playlistID=" + encodeURIComponent(playlistID), null, function(request) {
                if (request.readyState === 4) {
                    if (request.status === 200) {
                        let playlistSongs = JSON.parse(request.responseText);
                        if (playlistSongs.length === 0) {
							self.reset();
                            visibleAlert("The playlist is empty", 0);
                            return;
                        } else {
	                        self.playlistSongs = playlistSongs;
	                        self.currentIndex = 0;
	                        self.lastIndex = self.playlistSongs.length - 1;
	                        if (self.currentIndex < 5) {
								previousPageButton.style.display = "none";
							} else {
								previousPageButton.style.display = "block";
							}
	                        if (self.lastIndex - self.currentIndex < 5) {
								nextPageButton.style.display = "none";
							} else {
								nextPageButton.style.display = "block";
							}
							hiddenInput = document.getElementById("hiddenPlaylistID");
							hiddenInput.value = playlistID;
	                        self.update(); 
                        }
                    } else {
                        visibleAlert(request.responseText, 0);
                    }
                }
            });
	    }
	    
	    //Updates the view of the table
        this.update = function() {
			let maxIndex = Math.min(this.currentIndex + 4, this.lastIndex);
	        this.tableRow.innerHTML = "";
			//Creation of each item of the menù
	        for (let i = this.currentIndex; i <= maxIndex; i ++) {
	            let song = this.playlistSongs[i];
			    let cell = document.createElement("td");
			    let img = document.createElement("img");
			    img.src = "data:image/jpeg;base64," + song.imageFile;
			    img.style.width = '100px';
			    img.style.height = 'auto';
			    cell.appendChild(img);
			    let title = document.createElement("p");
			    title.textContent = song.songTitle;
			    cell.appendChild(title);
				//Adding the event listener on th click of the cell
				cell.addEventListener('click', function() {
		            updatePlayer(song.songID);
		            visiblePlayerPage();
		        });		
			    this.tableRow.appendChild(cell);
	        }
	    }
	    
	    //Reset the view of the table
	    this.reset = function() {
		    while (this.tableRow.firstChild) {
		        this.tableRow.removeChild(this.tableRow.firstChild);
		    }
		    this.playlistSongs = [];
		    this.currentIndex = 0;
		    this.lastIndex = 0;
		    previousPageButton.style.display = "none";
		    nextPageButton.style.display = "none";
		}
	    
	    //Shows the next five song (or less) of the playlist
	    this.showPreviousPage = function() {
			//Decrease of the current index
			if (this.currentIndex >= 5) {
				this.currentIndex -= 5;
			}
			//Hiding the button if it's the first page
			if (this.currentIndex === 0) {
				previousPageButton.style.display = "none";
			}
			nextPageButton.style.display = "block";
			this.update();
		}
		
		//Shows the previous five songs of the playlist
	    this.showNextPage = function() {
			//Increase of the current index
			if (this.currentIndex + 5 <= this.lastIndex) {
				this.currentIndex += 5;
			}
			//Hiding the button if it's the last page
			if (this.currentIndex + 5 > this.lastIndex) {
				nextPageButton.style.display = "none";
			}
			previousPageButton.style.display = "block";
			this.update();
		}
	}
	
	//List of the song that make up the drop-down menu of the playlist page
	function PlaylistSongSelectList(listContainer) {
		
		//Container of the list
		this.listContainer = listContainer;
	    	
    	//Gets the song list and shows the drop down menu
		this.show = function(playlistID) {	
		  	let self = this;
		  	//Servlet call to get the song list
		  	makeCall("GET", "getNotPlaylistSongs?playlistID=" + encodeURIComponent(playlistID), null, function(request) {
	      		if (request.readyState === 4) {
	            	if (request.status === 200) {
	              		let songList = JSON.parse(request.responseText);
	              		if (songList.length === 0) {
	                		visibleAlert("There are no songs to add to the playlist", 0);
	                		addPlaylistSongForm.style.display = "none";
	              		} else {
					 	 	addPlaylistSongForm.style.display = "block";
					    }
	              		self.update(songList);
            		} else {
              			visibleAlert(request.responseText, 1);
          			}
          		}
    		});
		}
		
		//Updates the view of the drop down menu
		this.update = function(arraySong) {
			let self = this;
			self.listContainer.innerHTML = '';
			//Creation of each item of the menù
	        arraySong.forEach(function(song) {
			    let option = document.createElement('option');
			    option.value = song.songID;
			    option.textContent = song.songTitle + " - " + song.albumTitle + " - " + song.artist + " - " + song.publicationYear;
			    self.listContainer.appendChild(option);
			});	
		}
	}
	
	//Gets the song and updates the player
	function updatePlayer(songID) {
		//Servlet call to get the song
	    makeCall("GET", "getPlayer?songID=" + encodeURIComponent(songID), null, function(request) {
	        if (request.readyState === 4) {
	            if (request.status === 200) {
	                let song = JSON.parse(request.responseText);
	                if (song == null) {
	                    visibleAlert("No song found", 1);
	                    return;
	                }
					//Updates the view of the player page
	                document.getElementById("playerSongTitle").textContent = song.songTitle;
	                document.getElementById("playerImage").src = 'data:image/jpeg;base64,' + song.imageFile;
	                document.getElementById("playerAlbumTitle").textContent = song.albumTitle;
	                document.getElementById("playerArtist").textContent = song.artist;
	                document.getElementById("playerPublicationYear").textContent = song.publicationYear;
	                document.getElementById("playerMusicalGenre").textContent = song.musicalGenre;
	                let audioPlayer = document.getElementById('audioPlayer');
	                let audioSource = document.getElementById('audioSource');
	                if (song.audioFile) {
	                    audioSource.src = 'data:audio/mpeg;base64,' + song.audioFile;
	                    audioPlayer.load();
	                    audioPlayer.style.display = 'block';
	                } else {
	                    audioPlayer.style.display = 'none';
	                }
	            } else {
	                visibleAlert("Error in fetching song details: " + request.responseText, 1);
	            }
	        }     
	    });
	}

	//Displays the section corresponding to the home page
	function visibleHomePage() {
		
		homePage.style.display = "block";
		playlistPage.style.display = "none";
		playerPage.style.display = "none";
	}
	
	//Displays the section corresponding to the playlist page
	function visiblePlaylistPage() {
		
		homePage.style.display = "none";
		playlistPage.style.display = "block";
		playerPage.style.display = "none";
	}
	
	//Displays the section corresponding to the player page
	function visiblePlayerPage() {
		
		homePage.style.display = "none";
		playlistPage.style.display = "none";
		playerPage.style.display = "block";
	}
	
	//Displays the alert section with the input message
	function visibleAlert (alertMessage, isError){
		if (isError == 1) {
			alertContainer.classList.add("alert-danger");
			alertContainer.classList.remove("alert-success");
		} else {
			alertContainer.classList.remove("alert-danger");
			alertContainer.classList.add("alert-success");
		}
		alertContainer.textContent = alertMessage;
		alertContainer.style.display = "block";
	}
}
