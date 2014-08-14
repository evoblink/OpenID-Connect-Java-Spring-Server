/*******************************************************************************
 * Copyright 2014 The MITRE Corporation 
 *   and the MIT Kerberos and Internet Trust Consortium
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
var TaxeUserModel = Backbone.Model.extend({

    idAttribute: "id",

    initialize: function () {

        // bind validation errors to dom elements
        // this will display form elements in red if they are not valid
        this.bind('error', function(model, errs) {
            _.map(errs, function (val, elID) {
                $('#' + elID).addClass('error');
            });
        });

    },

    // We can pass it default values.
    defaults:{
        
        id: "",
        sub: "",
        preferredUsername: "username",
        name: "Demo User",
        givenName: null,
        familyName: null,
        middleName: null,
        nickname: null,
        profile: null,
        picture: null,
        website: null,
        email: "user@example.com",
        emailVerified: true,
        gender: null,
        zoneinfo: null,
        locale: null,
        phoneNumber: null,
        phoneNumberVerified: null,
        address: null,
        updatedTime: null,
        birthdate: null,
        createdAt: null
    },

    urlRoot:"api/taxeusers",
    
    matches:function(term) {
    	
    	var matches = [];
    	
    	if (term) {
    		if (this.get('id').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('id');
    		} 
    		if (this.get('sub') != null && this.get('sub').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('sub');
    		} 
    		if (this.get('preferredUsername') != null && this.get('preferredUsername').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('preferredUsername');
    		} 
    		if (this.get('name') != null && this.get('name').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('name');
    		}
    		if (this.get('givenName') != null && this.get('givenName').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('givenName');
    		}
    		if (this.get('familyName') != null && this.get('familyName').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('familyName');
    		}
    		if (this.get('middleName') != null && this.get('middleName').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('middleName');
    		}
    		if (this.get('nickname') != null && this.get('nickname').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('nickname');
    		}
    		if (this.get('profile') != null && this.get('profile').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('profile');
    		}
    		if (this.get('picture') != null && this.get('picture').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('picture');
    		}
    		if (this.get('website') != null && this.get('website').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('website');
    		}
    		if (this.get('email') != null && this.get('email').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('email');
    		}
    		if (this.get('emailVerified') != null && this.get('emailVerified').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('emailVerified');
    		}
    		if (this.get('gender') != null && this.get('gender').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('gender');
    		}
    		if (this.get('zoneinfo') != null && this.get('zoneinfo').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('zoneinfo');
    		}
    		if (this.get('locale') != null && this.get('locale').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('locale');
    		}
    		if (this.get('phoneNumber') != null && this.get('phoneNumber').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('phoneNumber');
    		}
    		if (this.get('phoneNumberVerified') != null && this.get('phoneNumberVerified').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('phoneNumberVerified');
    		}
    		if (this.get('address') != null && this.get('address').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('address');
    		}
    		if (this.get('updatedTime') != null && this.get('updatedTime').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('updatedTime');
    		}
    		if (this.get('birthdate') != null && this.get('birthdate').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('birthdate');
    		}
    		if (this.get('createdAt') != null && this.get('createdAt').toLowerCase().indexOf(term.toLowerCase()) != -1) {
    			matches.push('createdAt');
    		}
                
    	} else {
    		// there's no search term, we always match
    		
	    	this.unset('matches', {silent: true});
	    	console.log('no term');
	    	return true;
    	}
    
    	
    	var matchString = matches.join(' | ');
    	
	    if (matches.length > 0) {
	    	this.set({
	    		matches: matchString
	    	}, {silent: true});
	    	
	    	return true;
	    } else {
	    	this.unset('matches', {silent: true});
	    	
	    	return false;
	    }
    }

});

var TaxeUserCollection = Backbone.Collection.extend({

    initialize: function() {
        //this.fetch();
    },

    model:TaxeUserModel,
    url:"api/taxeusers",
    
    getBySub: function(sub) {
		var taxeusers = this.where({sub: sub});
		if (taxeusers.length == 1) {
			return taxeusers[0];
		} else {
			return null;
		}
    }
});

var TaxeUserView = Backbone.View.extend({

    tagName: 'tr',

    initialize:function (options) {
    	this.options = options;

        if (!this.template) {
            this.template = _.template($('#tmpl-taxeuser').html());
        }

//        if (!this.scopeTemplate) {
//        	this.scopeTemplate = _.template($('#tmpl-scope-list').html());
//        }

//        if (!this.moreInfoTemplate) {
//        	this.moreInfoTemplate = _.template($('#tmpl-taxeuser-more-info-block').html());
//        }
        
        this.model.bind('change', this.render, this);
        
    },

    render:function (eventName) {
    	
    	var creationDate = this.model.get('createdAt');
		var displayCreationDate = "at an unknown time";
		var hoverCreationDate = "";
		if (creationDate == null || !moment(creationDate).isValid()) {
			displayCreationDate = "at an unknown time";
			hoverCreationDate = "";
		} else {
			creationDate = moment(creationDate);
			if (moment().diff(creationDate, 'months') < 6) {
				displayCreationDate = creationDate.fromNow();
			} else {
				displayCreationDate = "on " + creationDate.format("MMMM Do, YYYY");
			}
			hoverCreationDate = creationDate.format("MMMM Do, YYYY [at] h:mmA");
		}

    	
    	var json = {taxeuser: this.model.toJSON(), count: this.options.count, whiteList: this.options.whiteList, 
    			displayCreationDate: displayCreationDate, hoverCreationDate: hoverCreationDate};
        this.$el.html(this.template(json));

//        $('.scope-list', this.el).html(this.scopeTemplate({scopes: this.model.get('scope'), systemScopes: this.options.systemScopeList}));
        
//        $('.taxeuser-more-info-block', this.el).html(this.moreInfoTemplate({taxeuser: this.model.toJSON()}));
        
        $('.taxeuserid-full', this.el).hide();
        
        this.$('.dynamically-registered').tooltip({title: 'This taxeuser was dynamically registered'});
        this.$('.allow-introspection').tooltip({title: 'This taxeuser can perform token introspection'});
        
        this.updateMatched();
        
        return this;
    },
    
    updateMatched:function() {
    	
    	console.log(this.model.get('matches'));
    	
    	if (this.model.get('matches')) {
    		$('.matched', this.el).show();
    		$('.matched span', this.el).html(this.model.get('matches'));
    	} else {
    		$('.matched', this.el).hide();
    	}
    },

    events:{
        "click .btn-edit-taxeuser":"editTaxeUser",
        "click .btn-delete-taxeuser":"deleteTaxeUser",
//        "click .btn-whitelist":"whiteListTaxeUser",
        'click .toggleMoreInformation': 'toggleMoreInformation',
        "click .taxeuserid-substring":"showSub"
    },

    editTaxeUser:function (e) {
    	e.preventDefault();
        app.navigate('admin/taxeuser/' + this.model.id, {trigger: true});
    },

//    whiteListTaxeUser:function(e) {
//    	e.preventDefault();
//    	if (this.options.whiteList == null) {
//    		// create a new one
//    		app.navigate('admin/whitelist/new/' + this.model.id, {trigger: true});
//    	} else {
//    		// edit the existing one
//    		app.navigate('admin/whitelist/' + whiteList.id, {trigger: true});
//    	}
//    },
    
    deleteTaxeUser:function (e) {
    	e.preventDefault();

        if (confirm("Are you sure sure you would like to delete this taxeuser?")) {
            var _self = this;

            this.model.destroy({
                success:function () {
                    _self.$el.fadeTo("fast", 0.00, function () { //fade
                        $(this).slideUp("fast", function () { //slide up
                            $(this).remove(); //then remove from the DOM
                            _self.parentView.togglePlaceholder();
                        });
                    });
                },
                error:function (error, response) {
            		console.log("An error occurred when deleting a taxeuser");
    
					//Pull out the response text.
					var responseJson = JSON.parse(response.responseText);
            		
            		//Display an alert with an error message
					$('#modalAlert div.modal-header').html(responseJson.error);
	        		$('#modalAlert div.modal-body').html(responseJson.error_description);
            		
        			 $("#modalAlert").modal({ // wire up the actual modal functionality and show the dialog
        				 "backdrop" : "static",
        				 "keyboard" : true,
        				 "show" : true // ensure the modal is shown immediately
        			 });
            	}
            });

            _self.parentView.delegateEvents();
        }

        return false;
    },

	toggleMoreInformation:function(e) {
		e.preventDefault();
		if ($('.moreInformation', this.el).is(':visible')) {
			// hide it
			$('.moreInformationContainer', this.el).removeClass('alert').removeClass('alert-info').addClass('muted');
			$('.moreInformation', this.el).hide('fast');
			$('.toggleMoreInformation i', this.el).attr('class', 'icon-chevron-right');
		
		} else {
			// show it
			$('.moreInformationContainer', this.el).addClass('alert').addClass('alert-info').removeClass('muted');
			$('.moreInformation', this.el).show('fast');
			$('.toggleMoreInformation i', this.el).attr('class', 'icon-chevron-down');
		}
	},
	
	showSub:function(e) {
		e.preventDefault();
		
		$('.taxeuserid-full', this.el).show();
		
	},
	
	close:function () {
        $(this.el).unbind();
        $(this.el).empty();
    }
});

var TaxeUserListView = Backbone.View.extend({

    tagName: 'span',

    initialize:function (options) {
    	this.options = options;
    	this.filteredModel = this.model;
    },
    
    load:function(callback) {
    	if (this.model.isFetched &&
    			this.options.whiteListList.isFetched &&
    			this.options.stats.isFetched &&
    			this.options.systemScopeList.isFetched) {
    		callback();
    		return;
    	}
    	
    	$('#loadingbox').sheet('show');
    	$('#loading').html('<span class="label" id="loading-taxeusers">TaxeUsers</span> '
    			+ '<span class="label" id="loading-whitelist">Whitelist</span> '
    			+ '<span class="label" id="loading-scopes">Scopes</span> '
    			//+ '<span class="label" id="loading-stats">Statistics</span> ' 
    			);

    	$.when(this.model.fetchIfNeeded({success:function(e) {$('#loading-taxeusers').addClass('label-success');}}),
    			this.options.whiteListList.fetchIfNeeded({success:function(e) {$('#loading-whitelist').addClass('label-success');}}),
    			//this.options.stats.fetchIfNeeded({success:function(e) {$('#loading-stats').addClass('label-success');}}),
    			this.options.systemScopeList.fetchIfNeeded({success:function(e) {$('#loading-scopes').addClass('label-success');}}))
    			.done(function() {
    	    		$('#loadingbox').sheet('hide');
    	    		callback();
    			});
    	
    },

    events:{
        "click .new-taxeuser":"newTaxeUser",
        "click .refresh-table":"refreshTable",
        'keyup .search-query':'searchTable',
        'click .form-search button':'clearSearch',
        'page .paginator':'changePage'
    },

    newTaxeUser:function (e) {
    	e.preventDefault();
        this.remove();
        app.navigate('admin/taxeuser/new', {trigger: true});
    },

    render:function (eventName) {

        // append and render table structure
        $(this.el).html($('#tmpl-taxeuser-table').html());
        
        this.renderInner();

        return this;        
    },
    
    renderInner:function(eventName) {

        // render the rows
    	_.each(this.filteredModel.models, function (taxeuser, index) {
    		var view = new TaxeUserView({
				model:taxeuser, 
				count:this.options.stats.get(taxeuser.get('id')),
				systemScopeList: this.options.systemScopeList,
				whiteList: ""//this.options.whiteListList.getBySub(taxeuser.get('sub'))
			});
    		view.parentView = this;
    		var element = view.render().el;
            $("#taxeuser-table",this.el).append(element);
            if (Math.ceil((index + 1) / 10) != 1) {
            	$(element).hide();
            }
        }, this);

        this.togglePlaceholder();

        
    },
    
	togglePlaceholder:function() {
        // set up pagination
        var numPages = Math.ceil(this.filteredModel.length / 10);
        if (numPages > 1) {
        	$('.paginator', this.el).show();
        	$('.paginator', this.el).bootpag({
        		total: numPages,
        		page: 1
        	});        	
        } else {
        	$('.paginator', this.el).hide();
        }

		if (this.filteredModel.length > 0) {
			$('#taxeuser-table', this.el).show();
			$('#taxeuser-table-empty', this.el).hide();
			$('#taxeuser-table-search-empty', this.el).hide();
		} else {
			if (this.model.length > 0) {
				// there's stuff in the model but it's been filtered out
				$('#taxeuser-table', this.el).hide();
				$('#taxeuser-table-empty', this.el).hide();
				$('#taxeuser-table-search-empty', this.el).show();
			} else {
				// we're empty
				$('#taxeuser-table', this.el).hide();
				$('#taxeuser-table-empty', this.el).show();
				$('#taxeuser-table-search-empty', this.el).hide();
			}
		}
	},
	
	changePage:function(event, num) {
		$('.paginator', this.el).bootpag({page:num});
		$('#taxeuser-table tbody tr', this.el).each(function(index, element) {
			if (Math.ceil((index + 1) / 10) != num) {
            	$(element).hide();
            } else {
            	$(element).show();
            }
		});
	},
	
    refreshTable:function(e) {
    	e.preventDefault();
    	$('#loadingbox').sheet('show');
    	$('#loading').html('<span class="label" id="loading-taxeusers">TaxeUsers</span> ' +
    			'<span class="label" id="loading-whitelist">Whitelist</span> ' + 
    			'<span class="label" id="loading-scopes">Scopes</span> ' + 
    			'<span class="label" id="loading-stats">Statistics</span> ' 
    			);

    	var _self = this;
    	$.when(this.model.fetch({success:function(e) {$('#loading-taxeusers').addClass('label-success');}}),
    			this.options.whiteListList.fetch({success:function(e) {$('#loading-whitelist').addClass('label-success');}}),
    			this.options.stats.fetch({success:function(e) {$('#loading-stats').addClass('label-success');}}),
    			this.options.systemScopeList.fetch({success:function(e) {$('#loading-scopes').addClass('label-success');}}))
    			.done(function() {
    	    		$('#loadingbox').sheet('hide');
    	    		_self.render();
    			});
    },
    
    searchTable:function(e) {
    	var term = $('.search-query', this.el).val();
    	
		this.filteredModel = new TaxeUserCollection(this.model.filter(function(taxeuser) {
			return taxeuser.matches(term);
		}));
    	
    	// clear out the table
    	$('tbody', this.el).html('');
    	
    	// re-render the table
    	this.renderInner();
    	
    },
    
    clearSearch:function(e) {
    	$('.search-query', this.el).val('');
    	this.searchTable();
    }
    
    
});

var TaxeUserFormView = Backbone.View.extend({

    tagName:"span",

    initialize:function (options) {
    	this.options = options;

        if (!this.template) {
            this.template = _.template($('#tmpl-taxeuser-form').html());
        }
        
        if (!this.taxeuserSavedTemplate) {
        	this.taxeuserSavedTemplate = _.template($('#tmpl-taxeuser-saved').html());
        }

        this.redirectUrisCollection = new Backbone.Collection();
        this.scopeCollection = new Backbone.Collection();
        this.contactsCollection = new Backbone.Collection();
        this.defaultAcrValuesCollection = new Backbone.Collection();
        this.requestUrisCollection = new Backbone.Collection();
        // TODO: add Spring authorities collection and resource IDs collection?
    },

    events:{
        "click .btn-save":"saveTaxeUser",
        "click #allowRefresh" : "toggleRefreshTokenTimeout",
        "click #disableAccessTokenTimeout" : function() { 
        	$("#access-token-timeout-time", this.$el).prop('disabled',!$("#access-token-timeout-time", this.$el).prop('disabled')); 
        	$("#access-token-timeout-unit", this.$el).prop('disabled',!$("#access-token-timeout-unit", this.$el).prop('disabled')); 
        	document.getElementById("access-token-timeout-time").value = '';
        	},
        "click #disableRefreshTokenTimeout" : function() { 
        	$("#refresh-token-timeout-time", this.$el).prop('disabled',!$("#refresh-token-timeout-time", this.$el).prop('disabled'));
        	$("#refresh-token-timeout-unit", this.$el).prop('disabled',!$("#refresh-token-timeout-unit", this.$el).prop('disabled')); 
        	document.getElementById("refresh-token-timeout-time").value = ''; 	
        	},
        "click .btn-cancel":"cancel",
        "change #tokenEndpointAuthMethod input:radio":"toggleTaxeUserCredentials",
        "change #displayTaxeUserSecret":"toggleDisplayTaxeUserSecret",
        "change #generateTaxeUserSecret":"toggleGenerateTaxeUserSecret",
        "change #logoUri input":"previewLogo"
    },

    cancel:function(e) {
    	e.preventDefault();
    	app.navigate('admin/taxeusers', {trigger: true});
    },
    
	load:function(callback) {
    	if (this.options.systemScopeList.isFetched) {
    		$('#loadingbox').sheet('hide');
    		callback();
    		return;
    	}

    	if (this.model.get('id') == null) {
    		// only show the box if this is a new taxeuser, otherwise the box is already showing
	    	$('#loadingbox').sheet('show');
	    	$('#loading').html('<span class="label" id="loading-scopes">Scopes</span> ');
    	}

    	$.when(this.options.systemScopeList.fetchIfNeeded({success:function(e) {$('#loading-scopes').addClass('label-success');}}))
    	.done(function() {
    	    		$('#loadingbox').sheet('hide');
    	    		callback();
    			});    	
	},
    	
    toggleRefreshTokenTimeout:function () {
        $("#refreshTokenValidityTime", this.$el).toggle();
    },
    
    previewPicture:function() {
    	if ($('#taxeuser-picture input', this.el).val()) {
    		$('#taxeuser-picturePreview', this.el).empty();
    		$('#taxeuser-picturePreview', this.el).attr('src', $('#taxeuser-picture input', this.el).val());
    	} else {
    		//$('#logoBlock', this.el).hide();
    		$('#taxeuser-picturePreview', this.el).attr('src', 'resources/images/logo_placeholder.gif');
    	}
    },

    /**
     * Set up the form based on the current state of the tokenEndpointAuthMethod parameter
     * @param event
     */
    toggleTaxeUserCredentials:function() {
    	
        var tokenEndpointAuthMethod = $('#tokenEndpointAuthMethod input', this.el).filter(':checked').val();
        
        if (tokenEndpointAuthMethod == 'SECRET_BASIC' 
        	|| tokenEndpointAuthMethod == 'SECRET_POST'
        	|| tokenEndpointAuthMethod == 'SECRET_JWT') {
        	
        	// taxeuser secret is required, show all the bits
        	$('#taxeuserSecretPanel', this.el).show();
        	// this function sets up the display portions
        	this.toggleGenerateTaxeUserSecret();
        } else {
        	// no taxeuser secret, hide all the bits
        	$('#taxeuserSecretPanel', this.el).hide();        		        	
        }
        
        // show or hide the signing algorithm method depending on what's selected
        if (tokenEndpointAuthMethod == 'PRIVATE_KEY'
        	|| tokenEndpointAuthMethod == 'SECRET_JWT') {
        	$('#tokenEndpointAuthSigningAlg', this.el).show();
        } else {
        	$('#tokenEndpointAuthSigningAlg', this.el).hide();
        }
    },
    
    /**
     * Set up the form based on the "Generate" checkbox
     * @param event
     */
    toggleGenerateSub:function() {

    	if ($('#generateSub input', this.el).is(':checked')) {
    		// show the "generated" block, hide the "display" checkbox
    		$('#displaySub', this.el).hide();
    		$('#taxeuserSecret', this.el).hide();
    		$('#taxeuserSecretGenerated', this.el).show();
    		$('#taxeuserSecretHidden', this.el).hide();
    	} else {
    		// show the display checkbox, fall back to the "display" logic
    		$('#displaySub', this.el).show();
    		this.toggleDisplaySub();
    	}
    },
    
    /**
     * Handle whether or not to display the taxeuser secret
     * @param event
     */
    toggleDisplaySub:function() {

    	if ($('#displaySub input').is(':checked')) {
    		// want to display it
    		$('#taxeuserSecret', this.el).show();
    		$('#taxeuserSecretHidden', this.el).hide();
    		$('#taxeuserSecretGenerated', this.el).hide();
    	} else {
    		// want to hide it
    		$('#taxeuserSecret', this.el).hide();
    		$('#taxeuserSecretHidden', this.el).show();
    		$('#taxeuserSecretGenerated', this.el).hide();
    	}
    },

    // rounds down to the nearest integer value in seconds.
    getFormTokenNumberValue:function(value, timeUnit) {
        if (value == "") {
        	return null;
        } else if (timeUnit == 'hours') {
        	return parseInt(parseFloat(value) * 3600);
        } else if (timeUnit == 'minutes') {
        	return parseInt(parseFloat(value) * 60);
        } else { // seconds
        	return parseInt(value);
        }
    },
    
    // returns "null" if given the value "default" as a string, otherwise returns input value. useful for parsing the JOSE algorithm dropdowns
    defaultToNull:function(value) {
    	if (value == 'default') {
    		return null;
    	} else {
    		return value;
    	}
    },
    
    disableUnsupportedJOSEItems:function(serverSupported, query) {
        var supported = ['default'];
        if (serverSupported) {
        	supported = _.union(supported, serverSupported);
        }
        $(query, this.$el).each(function(idx) {
        	if(_.contains(supported, $(this).val())) {
        		$(this).prop('disabled', false);
        	} else {
        		$(this).prop('disabled', true);
        	}
        });
    	
    },

    // maps from a form-friendly name to the real grant parameter name
    grantMap:{
    	'authorization_code': 'authorization_code',
    	'password': 'password',
    	'implicit': 'implicit',
    	'taxeuser_credentials': 'taxeuser_credentials',
    	'redelegate': 'urn:ietf:params:oauth:grant_type:redelegate',
    	'refresh_token': 'refresh_token'
    },
    
    // maps from a form-friendly name to the real response type parameter name
    responseMap:{
    	'code': 'code',
    	'token': 'token',
    	'idtoken': 'id_token',
    	'token-idtoken': 'token id_token',
    	'code-idtoken': 'code id_token',
    	'code-token': 'code token',
    	'code-token-idtoken': 'code token id_token'
    },
    
    saveTaxeUser:function (event) {

        $('.control-group').removeClass('error');

        var generateSub = $('#taxeuser-generateSub input').is(':checked');
        var taxeuserSecret = null;
        
        // whether or not the taxeuser secret changed
        var subChanged = false;
        
        if (!generateSub) {
                // if it's required but we're not generating it, send the value to preserve it
                taxeuserSecret = $('#taxeuser-sub input').val();

                // if it's not the same as before, offer to display it
                if (taxeuserSecret != this.model.get('sub')) {
                        subChanged = true;
                }
        } else {
                // it's being generated anew
                subChanged = true;
        }
        
        var accessTokenValiditySeconds = null;
        if (!$('disableAccessTokenTimeout').is(':checked')) {
        	accessTokenValiditySeconds = this.getFormTokenNumberValue($('#accessTokenValidityTime input[type=text]').val(), $('#accessTokenValidityTime select').val()); 
        }
        
        var idTokenValiditySeconds = this.getFormTokenNumberValue($('#idTokenValidityTime input[type=text]').val(), $('#idTokenValidityTime select').val()); 
        
        var refreshTokenValiditySeconds = null;
        if ($('#allowRefresh').is(':checked')) {

        	if ($.inArray('refresh_token', grantTypes) == -1) {
        		grantTypes.push('refresh_token');
        	}

        	if ($.inArray('offline_access', scopes) == -1) {
            	scopes.push("offline_access");            		
        	}

        	if (!$('disableRefreshTokenTimeout').is(':checked')) {
        		refreshTokenValiditySeconds = this.getFormTokenNumberValue($('#refreshTokenValidityTime input[type=text]').val(), $('#refreshTokenValidityTime select').val());
        	}
        }
        
        var attrs = {
//            id: "",
            sub: $('#taxeuser-sub input').val(),
            preferredUsername: $('#taxeuser-preferredUsername input').val(),
            name: $('#taxeuser-name input').val(),
            givenName: $('#taxeuser-givenName input').val(),
            familyName: $('#taxeuser-familyName input').val(),
            middleName: $('#taxeuser-middleName input').val(),
            nickname: $('#taxeuser-nickname input').val(),
            profile: $('#taxeuser-profile input').val(),
            picture: $('#taxeuser-picture input').val(),
            website: $('#taxeuser-website input').val(),
            email: $('#taxeuser-email input').val(),
            emailVerified: $('#etaxeuser-mailVerified input').is(':checked'),
            gender: $('#taxeuser-gender input').val(),
            zoneinfo: $('#taxeuser-zoneinfo input').val(),
            locale: $('#taxeuser-locale input').val(),
            phoneNumber: $('#taxeuser-phoneNumber input').val(),
            phoneNumberVerified: $('#etaxeuser-phoneNumberVerified input').is(':checked'),
            address: $('#taxeuser-address input').val(),
            createdAt: null,
            updatedTime: null,
            birthdate: $('#taxeuser-birthdate input').val()
        };

        // post-validate
        if (attrs["allowRefresh"] == false) {
            attrs["refreshTokenValiditySeconds"] = null;
        }

        if ($('#disableAccessTokenTimeout').is(':checked')) {
            attrs["accessTokenValiditySeconds"] = null;
        }

        if ($('#disableRefreshTokenTimeout').is(':checked')) {
            attrs["refreshTokenValiditySeconds"] = null;
        }

        // set all empty strings to nulls
        for (var key in attrs) {
        	if (attrs[key] === "") {
        		attrs[key] = null;
        	}
        }
        
        var _self = this;
        this.model.save(attrs, {
            success:function () {

            	$('#modalAlertLabel').html('Client Saved');
            	
            	var savedModel = {
            		id: _self.model.get('id'),
            		sub: _self.model.get('sub'),
            		taxeuserSecret: _self.model.get('taxeuserSecret'),
            		subChanged: subChanged
            	};
            	
            	$('#modalAlert .modal-body').html(_self.taxeuserSavedTemplate(savedModel));
            	
            	$('#modalAlert .modal-body #savedSub').hide();
            	
            	$('#modalAlert').on('click', '#taxeuserSaveShow', function(event) {
            		event.preventDefault();
            		$('#taxeuserSaveShow').hide();
            		$('#savedSub').show();
            	});
            	
            	$('#modalAlert').modal({
            		'backdrop': 'static',
            		'keyboard': true,
            		'show': true
            	});
            	
            	app.taxeUserList.add(_self.model);
                app.navigate('admin/taxeusers', {trigger:true});
            },
            error:function (error, response) {
        		console.log("An error occurred when deleting from a list widget");

				//Pull out the response text.
				var responseJson = JSON.parse(response.responseText);
        		
        		//Display an alert with an error message
				$('#modalAlert div.modal-header').html(responseJson.error);
        		$('#modalAlert div.modal-body').html(responseJson.error_description);
        		
    			 $("#modalAlert").modal({ // wire up the actual modal functionality and show the dialog
    				 "backdrop" : "static",
    				 "keyboard" : true,
    				 "show" : true // ensure the modal is shown immediately
    			 });
        	}
        });

        return false;
    },

    render:function (eventName) {

        $(this.el).html(this.template(this.model.toJSON()));
        
        var _self = this;

        // build and bind registered redirect URI collection and view
        _.each(this.model.get("redirectUris"), function (redirectUri) {
            _self.redirectUrisCollection.add(new URIModel({item:redirectUri}));
        });

//        $("#redirectUris .controls",this.el).html(new ListWidgetView({
//        	type:'uri', 
//        	placeholder: 'https://',
//        	collection: this.redirectUrisCollection}).render().el);
        
//        // build and bind scopes
//        _.each(this.model.get("scope"), function (scope) {
//            _self.scopeCollection.add(new Backbone.Model({item:scope}));
//        });

//        $("#scope .controls",this.el).html(new ListWidgetView({
//        	placeholder: 'new scope', 
//        	autocomplete: _.uniq(_.flatten(this.options.systemScopeList.pluck("value"))), 
//            collection: this.scopeCollection}).render().el);

//        // build and bind contacts
//        _.each(this.model.get('contacts'), function (contact) {
//        	_self.contactsCollection.add(new Backbone.Model({item:contact}));
//        });
        
//        $("#contacts .controls", this.el).html(new ListWidgetView({
//        	placeholder: 'new contact',
//        	collection: this.contactsCollection}).render().el);
        
        
//        // build and bind request URIs
//        _.each(this.model.get('requestUris'), function (requestUri) {
//        	_self.requestUrisCollection.add(new URIModel({item:requestUri}));
//        });
        
//        $('#requestUris .controls', this.el).html(new ListWidgetView({
//        	type: 'uri',
//        	placeholder: 'https://',
//        	collection: this.requestUrisCollection}).render().el);
        
        // build and bind default ACR values
//        _.each(this.model.get('defaultAcrValues'), function (defaultAcrValue) {
//        	_self.defaultAcrValuesCollection.add(new Backbone.Model({item:defaultAcrValue}));
//        });
        
//        $('#defaultAcrValues .controls', this.el).html(new ListWidgetView({
//        	placeholder: 'new ACR value',
//        	// TODO: autocomplete from spec
//        	collection: this.defaultAcrValuesCollection}).render().el);
        
        // build and bind 
        
        // set up token  fields
        if (!this.model.get("allowRefresh")) {
            $("#refreshTokenValidityTime", this.$el).hide();
        }

//        if (this.model.get("accessTokenValiditySeconds") == null) {
//            $("#access-token-timeout-time", this.$el).prop('disabled',true);
//            $("#access-token-timeout-unit", this.$el).prop('disabled',true);
//        }

//        if (this.model.get("refreshTokenValiditySeconds") == null) {
//            $("#refresh-token-timeout-time", this.$el).prop('disabled',true);
//            $("#refresh-token-timeout-unit", this.$el).prop('disabled',true);
//        }
        
        // toggle other dynamic fields
//        this.toggleClientCredentials();
        this.previewPicture();
        
        // disable unsupported JOSE algorithms
//        this.disableUnsupportedJOSEItems(app.serverConfiguration.request_object_signing_alg_values_supported, '#requestObjectSigningAlg option');
//        this.disableUnsupportedJOSEItems(app.serverConfiguration.userinfo_signing_alg_values_supported, '#userInfoSignedResponseAlg option');
//        this.disableUnsupportedJOSEItems(app.serverConfiguration.userinfo_encryption_alg_values_supported, '#userInfoEncryptedResponseAlg option');
//        this.disableUnsupportedJOSEItems(app.serverConfiguration.userinfo_encryption_enc_values_supported, '#userInfoEncryptedResponseEnc option');
//        this.disableUnsupportedJOSEItems(app.serverConfiguration.id_token_signing_alg_values_supported, '#idTokenSignedResponseAlg option');
//        this.disableUnsupportedJOSEItems(app.serverConfiguration.id_token_encryption_alg_values_supported, '#idTokenEncryptedResponseAlg option');
//        this.disableUnsupportedJOSEItems(app.serverConfiguration.id_token_encryption_enc_values_supported, '#idTokenEncryptedResponseEnc option');
//        this.disableUnsupportedJOSEItems(app.serverConfiguration.token_endpoint_auth_signing_alg_values_supported, '#tokenEndpointAuthSigningAlg option');
        
        this.$('.nyi').clickover({
        	placement: 'right', 
        	title: 'Not Yet Implemented', 
        	content: 'The value of this field will be saved with the taxeuser, '
        		+'but the server does not currently process anything with it. '
        		+'Future versions of the server library will make use of this.'
        	});
        
       return this;
    }
});


