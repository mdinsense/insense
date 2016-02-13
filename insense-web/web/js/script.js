window.log = function(){
  log.history = log.history || [];  
  log.history.push(arguments);
  arguments.callee = arguments.callee.caller;  
  if(this.console) console.log( Array.prototype.slice.call(arguments) );
};
(function(b){function c(){}for(var d="assert,count,debug,dir,dirxml,error,exception,group,groupCollapsed,groupEnd,info,log,markTimeline,profile,profileEnd,time,timeEnd,trace,warn".split(","),a;a=d.pop();)b[a]=b[a]||c})(window.console=window.console||{});

function portlet_float_handler(button) {
	var doAction = true;
	button.target = '_blank';
	return doAction;
}

function portlet_delete_handler(button) {
	return wlp_deleteButtonDialog(button);
}

/* ===================================================== TRIMS ========================================= 
 * Add a TRIM function to the String object
 */
String.prototype.trim = function () {
	return this.replace(/^\s+|\s+$/, "");
};
String.prototype.ltrim = function () {
    return this.replace(/^\s+/,'');
};
String.prototype.rtrim = function () {
	return this.replace(/\s+$/,'');
};

/* --------------------------------------

	TIAACREF object

*/
/*global $: true, tcoDeferred: true */


var tiaacref = function () {
	"use strict";
};
tiaacref.defaults = {
	cookie: {
	},
	toggleMessage: {
	},
	clone: {
	},
	isNumber: {
	},
	checkNumber: {
	},
	supportedBrowser: {
	},
	deferredProcessing: {
	},
	interPortletCommunications: {
	},
	serializeDate: {
	},
	isValidDate: {
	},
	createId: {
		idCount: -1,
		name: 'tcinId'
	},
	independentElement: {
		timeout: 2000,
		shorterTimeout: 500,
		type: 'tooltip',
		validType: {
			tooltip: '',
			dropdownmenu: ''
		}
	},
	isIE: {
		ie: null,
		ie6: null,
		ie7: null,
		ie8: null
	},
	format: {
		standard: '#0.2',
		months: {
			shortName: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
			longName: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
		}
	},
	serverEnvironment: {
		analyticsTags: {
			prod: 'tiaamain',
			testST4: 'tiaamaindev',
			testST2: 'tiaamaindev',
			testIT2: 'tiaamaindev',
			testAT: 'tiaamaindev',
			testIT1: 'tiaamaindev',
			prodfix: 'tiaamainprodfix',
			'default': 'tiaamaindev'
		},
		gomezTags: {
			prod: 'PROD_',
			testST4: 'ST4_',
			testST2: 'ST2_',
			testIT2: 'IT2_',
			testIT1: 'IT1_',
			testAT: 'AT_',
			prodfix: 'PFX_',
			'default': 'DEV_'
		},
		serverList: {
			'ais4.tiaa-cref.org': 'prod',
			'www.tiaa-cref.org': 'prod',
			'secure.tiaa-cref.org': 'prod',
			'search.tiaa-cref.org': 'prod',
			'tiaa-cref.org': 'prod',
			'tcasset.org': 'prod',
			'securebanking.tiaa-cref.org': 'prod',
			'www.tiaa-crefinstitute.org': 'prod',
			'tiaa-cref.com': 'prod',
			'www.tiaacref.org': 'prod',
			'planningat.tiaa-cref.org': 'prod',
			'planning.tiaa-cref.org': 'prod',
			'www.tiaa.com': 'prod',
			'www.tiaa-cref.org:443': 'prod',
			'www.tiaa-cref.org:80': 'prod',
			'www4.tiaa-cref.org': 'prod',
			'www3.tiaa-cref.org': 'prod',
			'www1.tiaa-cref.org': 'prod',
			'www.tiaacref.com': 'prod',
			'www.tiaacreff.com': 'prod',
			'becomeyourfutureyou.com': 'prod',
			'plansponsor.ibbotson.com': 'prod',
			'www.tiaa-cref.com': 'prod',
			'publictools.tiaa-cref.org': 'prod',
			'tclifemgroup.tiaa-cref.org': 'prod',
			'secure.opinionlab.com': 'prod',
			'espanol.tiaa-cref.org': 'prod',
			'enroll.tiaa-cref.org': 'prod',
			'www.tiaadirect.org': 'prod',
			'www.tiaadirect.com': 'prod',
			'iwc.tiaa-cref.org': 'prod',
			'easwebportal.tiaa-cref.org': 'prod',
			'easwebportal.tiaa-cref.org': 'prod',
			'www.myretirement.org': 'prod',
			'www.retirementatwork.org': 'prod',
			'www.aarpcollegesavings.com': 'prod',
			'www.knowhowtoachieve.com': 'prod',
			'planfocus.tiaa-cref.org': 'prod',
			'ifs3.tiaa-cref.org': 'prod',
			'www.aarpcollegesavings.com': 'prod',
			'www.aarpvalidation.com': 'prod',
			'techresourcecenter.tiaa-cref.org': 'prod',
			'salestoolkit.tiaa-cref.org': 'prod',
			'ifa.tiaa-cref.org': 'prod',
			'archimedes.com': 'prod',
			'www.archimedes.com': 'prod',
			'tclifesolutions.tiaa-cref.org': 'prod',
			'360.tiaa-cref.org': 'prod',
			'www.intelligentvariableannuitytool.org': 'prod',
			'securepfx.ops.tiaa-cref.org': 'prodfix',
			'origin-planfocus-st2.test.tiaa-cref.org': 'testST2',
			'origin-planfocus-st4.test.tiaa-cref.org': 'testST4',
			'origin-planfocus-it2.test.tiaa-cref.org': 'testIT2',
			'origin-planfocus-at.test.tiaa-cref.org': 'testAT',
			'origin-publictools-st4.test.tiaa-cref.org': 'testST4',
			'origin-publictools-st2.test.tiaa-cref.org': 'testST2',
			'origin-publictools-it2.test.tiaa-cref.org': 'testIT2',
			'origin-publictools-at.test.tiaa-cref.org': 'testAT',
			'origin-ifa-st2.test.tiaa-cref.org': 'testST2',
			'unifieddesktop-st2.test.tiaa-cref.org': 'testST2',
			'ud-st2.test.tiaa-cref.org': 'testST2',
			'ud-st4.test.tiaa-cref.org': 'testST4',
			'ud-it1.test.tiaa-cref.org': 'testIT1',
			'unifieddesktop-pf.test.tiaa-cref.org': 'prodfix',
			'unifieddesktop-at.test.tiaa-cref.org': 'testAT',
			'ud.tiaa-cref.org': 'prod'

			}
	},
	tooltip: {
		size: 'medium',
		icon: 'tipLink',
		text: '',
		id: '',
		tooltip: '',
		tooltipSuffix: 'tooltip',
		position: 'right',
		clear: false,
		detached: false,
		attachTo: '',
		style: 'normal',
		validStyle: {
			normal: '',
			modal: '',
			sticky: ''
		},
		inline: true,
		validSize: {
			small: {width: 150, height: 'auto'},
			medium: {width: 250, height: 'auto'},
			large: {width: 400, height: 'auto'},
			auto: {width: 'auto', height: 'auto'}
		},
		positionValues: {
			left: 'pointerRight',
			top: 'pointerBottom',
			right: 'pointerLeft',
			bottom: 'pointerTop'
		},
		onClick: function(e) {
			"use strict";
			return true;
		},
		eventName: {
			activate: 'tooltipActivate'
		}
	},
	popup: {
		size: 'medium',
		fadeInterval: 250,
		popupSuffix: '_popup',
		maskSuffix: '_mask',
		type: '',
		detached: false,
		attachTo: '',
		newWindow: {
			name: 'tcWindow',
			status: 'no',
			toolbar: 'no',
			resizable: 'yes',
			scrollbars: 'yes',
			directory: 'no',
			location: 'no',
			menubar: 'no'
		},
		validSize: {
			small: {width: 300, height: 300},
			medium: {width: 600, height: 600},
			large: {width: 900, height: 900},
			auto: {width: 'auto', height: 'auto'},
			parent: {width: 0, height: 0}
		},
		onClose: function (evt) {
			"use strict";
			return true;
		},
		onOpen: function (evt) {
			"use strict";
			return true;
		},
		eventName: {
			open: 'popupOpen',
			close: 'popupClose'
		}
	},
	paginate: {
		id: '',
		table: '',
		startPage: 1,
		perPage: 20,
		expandCollapse: false,
		connectWith: {},
		autoCount: false,
		multiSort: false,
		dataSource: '',
		showPerPageDropDown: true,
		pageListingLimit: 15,
		perPageDropDownValues: [10, 25, 50],
		style: 'normal',
		showViewingText: false,
		validStyle: {
			ud: '',
			normal: '',
			'new': ''
		},
		eventName: {
			numRecords: 'paginateNumRecords',
			currentPage: 'paginate',
			drawTable: 'drawTable',
			drawPagination: 'drawPagination',
			setTableNumRecords: 'setNumRecords',
			recalcDataSource: 'recalcDataSource',
			setDataSource: 'setDataSource',
			previousLink: 'paginatePrevious',
			nextLink: 'paginateNext',
			recordsPerPage: 'paginateRecordsPerPage',
			selectPage: 'paginateSelectPage'
		}
	},
	expandCollapse: {
		header: '',
		content: '',
		disabled: false,
		headerToggle: '',
		style: 'simple',
		startingCondition: 'collapsed',
		moreHideSize: 75,
		validStyle: {
			panel: {
				headerButton: 'h3 a.expanded, h3 a.collapsed',
//				headerButton: 'div.hd',
				headerClass: {expanded: 'hdexpanded', collapsed: ''},
				contentClass: {expanded: 'bgexpanded', collapsed: ''},
				buttonClass: {expanded: 'expanded', collapsed: 'collapsed'},
				disabledClass: 'hddisabled'
			},
			alert: {
				headerButton: 'a.expanded, a.collapsed',
				contentButton: 'a.expanded, a.collapsed',
				headerClass: {expanded: '', collapsed: ''},
				contentClass: {expanded: '', collapsed: ''},
				buttonClass: {expanded: 'expanded', collapsed: 'collapsed'},
				disabledClass: ''
			},
			simple: {
				headerButton: 'a.expanded, a.collapsed',
				headerClass: {expanded: '', collapsed: ''},
				contentClass: {expanded: '', collapsed: ''},
				buttonClass: {expanded: 'expanded', collapsed: 'collapsed'},
				disabledClass: ''
			},
			accountList: {
				headerButton: 'a.expanded, a.collapsed',
				headerClass: {expanded: 'bgexpanded', collapsed: ''},
				contentClass: {expanded: '', collapsed: ''},
				buttonClass: {expanded: 'expanded', collapsed: 'collapsed'},
				disabledClass: ''
			},
			showMore: {
				headerClass: {expanded: 'minusLink', collapsed: 'addLink'},
				text: {collapsed: 'Show More', expanded: 'Show Less'},
				collapsedSize: 3
			},
			learnMore: {
				headerClass: {expanded: '', collapsed: ''},
				contentClass: {expanded: '', collapsed: ''},
				text: {collapsed: 'Learn More &raquo;', expanded: 'Hide'}
			},
			transactionList: {
				headerButton: 'a.expanded, a.collapsed',
				headerClass: {expanded: 'bgExpandedTop', collapsed: ''},
				contentClass: {expanded: 'bgExpandedBottom', collapsed: ''},
				buttonClass: {expanded: 'expanded', collapsed: 'collapsed'},
				disabledClass: ''
			},
			moreHide: {
				headerClass: {expanded: 'hidden', collapsed: ''},
				contentClass: {expanded: '', collapsed: 'hidden'}
			}
		},
		validStartingCondition: {
			expanded: '',
			collapsed: ''
		},
		expandEventName: 'expandCollapseExpand',
		collapseEventName: 'expandCollapseCollapse',
		eventName: {
			expand: 'expandCollapseExpand',
			collapse: 'expandCollpaseCollapse'
		},
		onBeforeCollapse: function () {
			"use strict";
			return true;
		},
		onBeforeExpand: function () {
			"use strict";
			return true;
		},
		onAfterCollapse: function () {
			"use strict";
			return true;
		},
		onAfterExpand: function () {
			"use strict";
			return true;
		}
	},
	calendar: {
		id: '',
		constrainInput: false,
		minDate: '+0',
		style: '',
		selectedDate: null,
		maxDate: null,
		assistText: 'mm/dd/yyyy',
		showAssistText: false,
		disableWeekends: false,
		calendarOptions: {},
		setState: {
			state: 'normal',
			dates: ''
		},
		validState: {
			normal: [true, ""],
			disabled: [false, ""],
			available: [true, "adates"],
			notAvailable: [false, "udates"]
		},
		validStyle: {
			standard: '',
			monthyearselect: ''
		},
		events: {
			selectDateName: 'calendarSelectDate',
			showDayName: 'calendarShowDay',
			changeMonthYearName: 'calendarChangeMonthYear'
		},
		eventName: {
			selectDate: 'calendarSelectDate',
			showDay: 'calendarShowDay',
			changeMonthYear: 'calendarChangeMonthYear'
		},
		monthsType: 'long',
		validMonthsType: {
			long: '',
			short: ''
		}
	},
	autoComplete: {
		id: '',
		data: [],
		cache: {},
		minimumLength: 3,
		dataType: 'jsonp',
		listSuffix: '_list',
		serverParameters: '{}',
		validDataType: {
			xml: '',
			html: '',
			script: '',
			json: '',
			jsonp: '',
			text: ''
		},
		onSelect: function(event, ui) {
			"use strict";
			return;
		},
		eventName: {
			select: 'autoCompleteSelect',
			error: 'autoCompleteError',
			success: 'autoCompleteSuccess'
		}
	},
	tableSort: {
		id: '',
		caseSensitive: true,
		multiSort: false,
		initialSort: '',
		initialSortDirection: 'asc',
		moreHideSize: 75,
		multiColumnSort: false,
		onBeforeSort: function () {
			"use strict";
			return true;
		},
		onAfterSort: function () {
			"use strict";
			return true;
		},
		sortEventName: 'tableSort',
		events: {
			beforeSortName: 'tableSortBeforeSort',
			afterSortName: 'tableSortAfterSort'
		},
		eventName: {
			beforeSort: 'tableSortBeforeSort',
			afterSort: 'tableSortAfterSort',
			filter: 'tableSortFilter',
			reloadSortData: 'reloadSortData',
			applyFilter: 'tableSortApplyFilter',
			applySort: 'tableSortApplySort'
		}
	},
	map: {
		id: '',
		type: 'static',
		size: 'medium',
		typeValues: {
			'static': '',
			'dynamic': ''
		},
		sizeValues: {
			small: {
				width: 250,
				height: 200
			},
			medium: {
				width: 550,
				height: 350
			},
			large: {
				width: 650,
				height: 500
			}
		},
		transport: 'https',
		transportValues: {
			http: '',
			https: ''
		},
		address: '8625 Andrew Carnegie Blvd. Charlotte, NC 28262'
	},
	expandAll: {
		id: '',
		varList: '',
		title: '',
		startCondition: 'collapsed',
		startConditionValues: {
			collapsed: '',
			expanded: ''
		},
		parameters: {
			collapsed: {
				className: 'addLink',
				text: 'Expand All',
				action: 'collapse',
				title: 'To expand all items'
			},
			expanded: {
				className: 'minusLink',
				text: 'Collapse All',
				action: 'expand',
				title: 'To collapse all items'
			}
		},
		expandEventName: 'expandAllExpand',
		collapseEventName: 'expandAllCollapse',
		eventName: {
			expand: 'expandAllExpand',
			collapse: 'expandAllCollapse',
			expanded: 'expandAllExpand',
			collapsed: 'expandAllCollapse'
		}
	},
	analytics: {
		location: 'https://www.tiaa-cref.org/ucm/groups/public/documents/webasset/js_site_catalyst_lazy.js',
		lazyLoad: true,
		initialPageTrack: true,
		linkType: 'o',
		validLinkType: {o: '', d: '', e: ''}
	},
	floatingCallout: {
		id: ''
	},
	input: {
		id: '',
		compareTo: '',
		type: 'text',
		validType: {
			text: '',
			compareTo: '',
			number: '',
			ajaxfile: ''
		},
		ajaxInputId: '',
		ajaxTargetId: '',
		excludedExtensions: [],
		includedExtensions: [],
		eventName: {
			uploadStarted: 'inputUploadStarted',
			uploadInProgress: 'inputUploadInProgress',
			uploadFinished: 'inputUploadFinished'
		}
	},
	tab: {
	},
	utilityLink: {
	},
	carousel: {
		id: '',
		delay: 10000,
		defaultDisplay: '',
		autoMove: true,
		style: 'marketing',
		adviceContent: '',
		animationDuration: 1000,
		adviceCarouselCount: 3,
		startPage: 1,
		validStyle: {
			marketing: '',
			advice: '',
			portfolio: '',
			simple: ''
		},
		nextSlideEventName: 'carouselNextSlide',
		previousSlideEventName: 'carouselPrevSlide',
		currentSlideEventName: 'carouselThisSlide',
		eventName: {
			nextSlide: 'carouselNextSlide',
			previousSlide: 'carouselPrevSlide',
			currentSlide: 'carouselThisSlide'
		}
			
	},
	button: {
		id: '',
		onClick: function(e) {
			return true;
		}
	},
	progressBar: {
		id: ''
	},
	legend: {
		id: '',
		data: {},
		style: 'piepercent',
		cssClass: '',
		format: '',
		showZeroPercentage: true,
		suppressValueDisplay: false,
		columnBreak: 0,
		showMore: 'Show Funds|Hide Funds',
		expanded: false,
		validStyle: {
			normal: {
				tableClass: 'chartTable',
				format: '%0.0'
			},
			piepercent: {
				tableClass: 'chartTable',
				format: '%0.0'
			},
			piepercentdecimal: {
				tableClass: 'chartTable',
				format: '%0.2'
			},
			piedollar: {
				tableClass: 'chartTable',
				format: '$0.2'
			},
			pieLTD: {
				tableClass: 'chartTable',
				format: '$0.2'
			},
			osda: {
				tableClass: 'funds chartTable',
				format: '%0.0'
			},
			none: {
				tableClass: '',
				format: ''
			},
			percentvalue: {
				tableClass: 'chartTable',
				format: '0.2'
			},
			custom: {
				tableClass: 'summary fw',
				format: '$0.2'
			}
		},
		eventName: {
			hover: 'legendHover'
		}
	},
	chart: {
		id: '',
		data: {},
		cssClass: '',
		style: 'piepercent',
		chartOptions: {},
		validStyle: {
			piepercent: {
				tableClass: 'chartTable',
				format: '%0.0'
			},
			piedollar: {
				tableClass: 'chartTable',
				format: '$0.2'
			}
		},
		type: 'pie',
		validType: {
			pie: {
				strokeWidth: 1,
				minimumPercentage: .015
			},
			bar: {},
			stackedBar: {},
			area: {},
			stackedArea: {},
			stockChart: {},
			line: {},
			custom: {}
		},
		size: 'medium',
		validSize: {
			small: {width: 50, height: 50},
			medium: {width: 140, height: 140},
			large: {width: 300, height: 300}
		},
		eventName: {
			pointHover: 'chartPointHover',
			rangeChange: 'rangeChange',
			pointSelect: 'pointSelect'
		}
	},
	data: {
		display: true, 
		format: '#0.2',
		exclude: false
	},
	series: {
		display: true, 
		exclude: false, 
		label: '',
		format: '#0.2',
		color: '',
		negativeColor: ''
	},
	point: {
		color: '', 
		negativeColor: '',
		display: true, 
		exclude: false, 
		label: '', 
		onClick: function(e) {
			return true;
		}, 
		tooltip: null,
		format: '#0.2',
		value: 0
	},
	nextColor: {
		colorList: [
		     '#005EB8', '#00A9E0', '#658D1B', '#84BD00', '#007377', '#009CA6', '#DC8633',
			 '#F0B323', '#76232F', '#A6192E', '#512D6D', '#93328E', '#53565A', '#75787B'
		],
		colorBlank: true
	},
	menu: {
		id: '',
		style: 'standard',
		megaMenuId: '',
		attachTo: '',
		validStyle: {
			standard: '',
			ira: '',
			megamenu: ''
		},
		eventName: {
			select: 'menuSelect',
			selectTab: 'menuSelectTab'
		}
	},
	error: {
		err: 'unknown',
		badId: 'Cannot find DOM element: %1 (%2)',
		typeError: "Variable type error on '%1'. Variable should be %2.",
		unknown: 'Undefined or unknown error (%1: %2: %3)'
	},
	dropDownMenu: {
		id: '',
		dropDownMenuId: '',
		detached: false,   
		position: 'right',
		eventName: {
			selectOption: 'dropDownMenuSelectOption'
		}
	},
	message: {
		id: '',
		style: 'success',
		closeable: false,
		insertType: 'standard',
		validInsertType: {
			standard: '',
			listing: ''
		},
		validStyle: {
			error: 'alertModule',
			info: 'infoModule',
			notice: 'noticeModule',
			inline: 'inlineAlert',
			success: 'successModule'
		}
	},
	opinion: {
		libraryLocation: 'framework/skins/tiaa/js/oo_engine.min.js',
		configurationLocation: 'framework/skins/tiaa/js/oo_conf.js',
		active: false,
		feedback: false
	},
	gomez: {
		libraryLocation: 'https://akamai.t.axf8.net/js/gtagb.js',
		active: false
	},
	animatedBar: {
		id: '',
		value: 0,
		style: 'animated',
		validStyle: {
			'animated': {cssClass: 'progress'},
			'static': {cssClass: 'progress'}
		},
		eventName: {
			setValue: 'setValue',
			valueChange: 'valueChange'
		}
	},
	slider: {
		id: '',
		minimumValue: 0,
		maximumValue: 100,
		selectedValue: {
			start: 0,
			end: 100
		},
		style: 'range',
		validStyle: {
			range: '',
			single: ''
		},
		type: 'numeric',
		validType: {
			numeric: {
				minimumValue: 0,
				maximumValue: 0,
				selectedValue: {
					start: 0,
					end: 100
				}
			},
			date: {
				minimumValue: -1,
				maximumValue: -1,
				selectedValue: {
					start: -1,
					end: -1
				}
			}
		},
		format: 'day',
		validFormat: {
			day: {
				parseExactFormat: 'M/d/yyyy',
				minimumValue: null,
				maximumValue: null
			},
			month: {
				parseExactFormat: 'M/yyyy',
				minimumValue: null,
				maximumValue: null
			}
		},
		eventName: {
			changeSlide: 'changeSlide'
		}
	},
	enhancedSelect: {
		id: '',
		size: 360,
		selectText: 'select',
		type: 'standard',
		validType: {
			standard: '',
			autoselect: {
				suffixes: {
					select: '_select',
					chosen: '_chosen'
				},
				cssClass: {
					select: 'chzn-select chzn-done',
					chosen: 'chzn-container chzn-container-multi chzn-container-active',
					dropDown: ''
				}
			}
		},
		eventName: {
			checked: 'enhancedSelectChecked',
			setCheckBox: 'enhancedSelectSetCheckBox'
		}
	},
	container: {
		id: '',
		style: 'standard',
		validStyle: {
			standard: '',
			bluearrow: ''
		}
	},
	TCApp: {
		description: '',
		cssClass: ''
	},
	TCAppContainer: {
		description: '',
		cssClass: '',
		context: {},
		params: {},
		enableBatchRequests: false,
		type: "GET",
		validType: {
			GET: null,
			POST: null
		},
		dataType: 'jsonp',
		iFrame: 'tcAppContainer'
	},
	timeout: {
		id: 'timeout',
		countInterval: 1
	},
	mail: {
		id: '',
		cssClass: '',
		style: '',
		eventName: {
			messageDeleted: 'messageDeleted',
			messageRead: 'messageRead',
			messageCurrent: 'messageCurrent',
//			deleteMarked: 'deleteMarked',
//			messageMarked: 'messageMarked',
			messageRegister: 'messageRegister',
			selectMessage: 'selectMessage',
			selectAll: 'selectAll',
			deleteSelected: 'deleteSelected',
			messageClick: 'messageClick',
			clearSelectAll: 'clearSelectAll',
			numberMessages: 'numberMessages',
			numberUnreadMessages: 'numberUnreadMessages'
		}
	},
	mailMessage: {
		id: '',
		cssClass: '',
		style: '',
		eventName: {
			home: 'home',
			next: 'next',
			previous: 'previous',
			'delete': 'delete',
			archive: 'archive',
			setDelete: 'setDelete',
			setArchive: 'setArchive',
			setRead: 'setRead'
		}
	},
	FileDoc: {
		id: '',
		style: '',
		eventName: {
			docDeleted: 'docDeleted',
			docDownload: 'docDownload',
			docMove: 'docMove',
			select: "select"	
		}
	},
	FileFolder: {
		id: '',
		style: '',
		eventName: {
			folderDeleted: 'docDeleted',
			folderAdd: 'docDownload',
			docRegister: "docRegister",	
			countselecteddocs: "countselecteddocs",
			selectedDoc: "selectedDoc",
			docDeleted: "docDeleted",
			docMove: "docMove",
			docLabel: "docLabel"
		}
	},
	FileManager: {
		id: '',
		style: '',
		eventName: {
			home: 'home',
			folderRegister: "folderRegister",	
			docSelected: "docSelected",
			folderSelected: "folderSelected",
			selectedDoc: "selectedDoc",
			docDeleted: "docDeleted",
			docMove: "docMove",
			docLabel: "docLabel",	
			folderLabel: "folderLabel",
			docpermdelete: "docpermdDelete"
		}
	}
};


/*
 * ------------------------- Ensighten -------------------------------
 */
tiaacref.useEnsighten = false;
/*
 * ------------------------ Regular Expression Escape -----------------------
 */
tiaacref.regExpEscape = function(string) {
    return string.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
};


/*
 * ---------------------------------- HideElement ----------------------------
 */
tiaacref.hideElement = function(element) {
	element.addClass('hidden').attr({'aria-hidden': true});
};
/*
 * -------------------------------- ShowElement ------------------------------
 */
tiaacref.showElement = function(element) {
	element.removeClass('hidden').attr({'aria-hidden': false});
};
/*
 * -------------------------------- IsHidden -------------------------
 */
tiaacref.isHidden = function(element) {
	return element.hasClass('hidden');
};

/*
 * --------------------- Parse Portal Properties ----------------------
 */
tiaacref.parsePortalProperties = function (arg) {
	"use strict";
	var variables,
		varList = '',
		valueList = '',
		i,
		l,
		tag,
		value,
		retval = {};
	if (typeof arg !== 'string') {
		return retval;
	}
	if (arg === '') {
		return retval;
	}
	variables = arg.split(';');
	l = variables.length;
	for(i = 0; i < l; i++) {
		tag = variables[i].split(':');
		if (tag.length > 1) {
			value = tag[1].trim();
			if (value === "true") {
				value = true;
			} else if (value === "false") {
				value = false;
			}
			retval[tag[0].trim()] = value;
		}
	}
	return retval;

};


/*
 * ---------------------------------- Timeout -----------------------------------
 */
tiaacref.timeout = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.timeout;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			countInterval: defaults.countInterval
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.countInterval !== 'number') {
			arg.countInterval = defaults.countInterval;
		}
	}
	this.arg = arg;
	this.timer = null;
	this.timeCount = 0;
};
tiaacref.timeout.prototype = {
	start: function() {
		var self = this;
		this.timeCount = 0;
		tcoipc.fire(self.arg.id + 'startTime', this.timeCount);
		this.timer = setInterval(function() {
			self.timeCount ++;
			tcoipc.fire(self.arg.id + 'duringTime', self.timeCount);
		}, this.arg.countInterval * 1000); 
	},
	stop: function() {
		clearInterval(this.timer);
		this.timer = null;
		tcoipc.fire(this.arg.id + 'stopTime', this.timeCount);
	},
	reset: function() {
		this.timeCount = 0;
		
	},
	getTimeCount: function() {
		return this.timeCount;
	},
	
	getTimer: function() {
		return this.timer;
	}
	
};




/*
 * ------------------------ Normalize Size --------------------------------------
 * 
 */
tiaacref.normalizeSize = function(size, defaults) {
	"use strict";
	var sizes,
		customSize = {
			height: 0, 
			width: 0,
			newSizeValue: size
		};
	if (typeof size === 'object') {
		if (typeof size.width === 'number' && typeof size.height === 'number') {
			customSize = {
				height: size.height,
				width: size.width,
				newSizeValue: 'custom'
			};
		} else {
			size = defaults.size;
		}
	} else {
		if (typeof defaults.validSize[size] === 'undefined') {
			if (size.indexOf(',') !== -1) {
				sizes = size.split(',');
				if (sizes.length === 2) {
					if (tiaacref.isNumber(sizes[0].trim()) && tiaacref.isNumber(sizes[1].trim())) {
						customSize = {
							width: parseInt(sizes[0].trim()), 
							height: parseInt(sizes[1].trim()),
							newSizeValue: 'custom'
						};
					} else {
						customSize.newSizeValue = defaults.size;
					}
				} else {
					customSize.newSizeValue = defaults.size;
				}
			} else {
				if (tiaacref.isNumber(size.trim())) {
					customSize = {
						width: parseInt(size.trim()), 
						height: parseInt(size.trim()),
						newSizeValue: 'custom'
					};
				} else {
					customSize.newSizeValue = defaults.size;
				}
			}
		} else {
			customSize = {
				height: defaults.validSize[size].height,
				width: defaults.validSize[size].width,
				newSizeValue: size
			};
		}
	}
	return customSize;
};


/* 
 * ------------------------------ Date object ------------------------------------------
 */
tiaacref.date = {
	day: {
		diff: function(date1, date2) {
			"use strict";
			if (!date1 instanceof Date) {
				throw new tiaacref.typeError('First date argument not a Date type.');
			}
			if (!date2 instanceof Date) {
				throw new tiaacref.typeError('Second date argument not a Date type.');
			}
			var diff = Math.round(Math.abs(date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000));
			return diff;
		}
	},
	month: {
		diff: function(date1, date2) {
			"use strict";
			if (!date1 instanceof Date) {
				throw new tiaacref.typeError('First date argument not a Date type.');
			}
			if (!date2 instanceof Date) {
				throw new tiaacref.typeError('Second date argument not a Date type.');
			}
			var monthDiff = Math.abs((date1.getFullYear()*12 + date1.getMonth()) - (date2.getFullYear()*12 + date2.getMonth()));
			return monthDiff;
		}
	},
	year: {
		diff: function(date1, date2) {
			"use strict";
			if (!date1 instanceof Date) {
				throw new tiaacref.typeError('First date argument not a Date type.');
			}
			if (!date2 instanceof Date) {
				throw new tiaacref.typeError('Second date argument not a Date type.');
			}
			var yearDiff = Math.abs(date1.getFullYear() - date2.getFullYear());
			return yearDiff;
		}
	}
};

/* ---------------------------------------- Event Name --------------------------- */
tiaacref.eventName = function(name) {
	"use strict";
	var defaults = tiaacref.defaults,
		query,
		eventClass,
		eventNameQuery;
	if (typeof name !== 'string') {
		throw new tiaacref.typeError("Argument 'name' not specified as a string.");
	}
	query = name.split('.');
	eventClass = query[0];
	if (typeof defaults[eventClass] === 'undefined') {
		throw tiaacref.dataNotDefined("Event name for event class '" + eventClass + "' cannot be found");
	}
	if (typeof query[1] === 'undefined') {
		throw tiaacref.dataNotDefined("Event name for event class '" + eventClass + "' is not specified. Use 'eventClass.eventName' as the argument.");
	}
	eventNameQuery = query[1];
	if (typeof defaults[eventClass].eventName[eventNameQuery] === 'undefined') {
		throw tiaacref.dataNotDefined("Event name '" + eventNameQuery + "' for event class '" + eventClass + "' cannot be found.");
	}
	return defaults[eventClass].eventName[eventNameQuery];
};


/* ------------------------------------------ Error -------------------------------- */
tiaacref.missingDOM = function(txt) {
	this.name = "Missing DOM";
	this.message = txt;
};
tiaacref.missingDOM.prototype = new Error();

tiaacref.typeError = function(txt) {
	this.name = "Type Error";
	this.message = txt;
};
tiaacref.typeError.prototype = new Error();

tiaacref.dataNotDefined = function(txt) {
	this.name = "Data Not Defined Error";
	this.message = txt;
};
tiaacref.dataNotDefined.prototype = new Error();

tiaacref.missingArgument = function(txt) {
	this.name = "Missing Argument";
	this.message = txt;
};
tiaacref.missingArgument.prototype = new Error();

tiaacref.error = function(err, txt1, txt2, txt3) {
	"use strict";
	var defaults = tiaacref.defaults.error,
		txt,
		errorMessage;
	if (typeof err === 'undefined') {
		err = defaults.err; 
	}
	if (typeof defaults[err] === 'undefined') {
		err = defaults.err;
	}
	txt1 = (typeof txt1 !== 'string' ? '' : txt1);
	txt2 = (typeof txt2 !== 'string' ? '' : txt2);
	txt3 = (typeof txt3 !== 'string' ? '' : txt3);
	txt = defaults[err].replace('%1',txt1).replace('%2',txt2).replace('%3',txt3);
	switch(err){
	case 'badId':
		errorMessage = new tiaacref.missingDOM(txt);
		break;
	default:
		errorMessage = new Error(txt);
		break;
	}
	return errorMessage;
};

/* ------------------------------------------- Get Cookie ------------------------------ */
tiaacref.getCookie = function(check_name) {
	var a_all_cookies = document.cookie.split( ';' ),
		a_temp_cookie = '',
		cookie_name = '',
		cookie_value = '',
		b_cookie_found = false,
		i,
		l = a_all_cookies.length;
	for (i = 0; i < l; i++ ) {
		a_temp_cookie = a_all_cookies[i].split( '=' );
		cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, '');
		if ( cookie_name == check_name ) {
			b_cookie_found = true;
			if ( a_temp_cookie.length > 1 ) {
				cookie_value = unescape( a_temp_cookie[1].replace(/^\s+|\s+$/g, '') );
			}
			return cookie_value;
			break;
		}
		a_temp_cookie = null;
		cookie_name = '';
	}
	if ( !b_cookie_found ) {
		return null;
	}
};

/* --------------------------------------- Set Cookie ----------------------------- */

tiaacref.setCookie = function(name, value, expires, path, domain, secure) {
	var today = new Date();
	today.setTime(today.getTime());
	if (expires) {
		expires = expires * 1000 * 60 * 60 * 24;
	}
	var expires_date = new Date(today.getTime() + (expires));
	document.cookie = name + "=" +escape( value ) +
	( ( expires ) ? ";expires=" + expires_date.toGMTString() : "" ) +
	( ( path ) ? ";path=" + path : "" ) +
	( ( domain ) ? ";domain=" + domain : "" ) +
	( ( secure ) ? ";secure" : "" );
};



/* ================================ TOGGLE MESSAGE =========================================== */
/* TODO: depreciate in next version. Only here for those apps that use old prototype code */
tiaacref.toggleMessage = function(classPrefix, parentObj, messageContent) {
	"use strict";
	var messageModule = parentObj.find('div.'+classPrefix+'Module:first'),
		speed = 300,
		method = 'easeOutSine',
		$moduleHeight;
	if (messageContent === '') {
		messageModule.stop().hide();
		messageModule.css('height','0px');
		if(classPrefix.match(/alert/)) {
			parentObj.find('div.alertHighlight,label.alertHighlight,span.alertHighlight').removeClass('alertHighlight');
		}
	} else {
		messageModule.find('div').html(messageContent);
		$moduleHeight = messageModule.css('paddingTop').replace(/[^0-9]+/g,'')*1 + messageModule.css('paddingBottom').replace(/[^0-9]+/g,'')*1;
		//TODO: Replace evenIfHidden with custom function.
		//		messageModule.find('div').evenIfHidden(function (element) {
		//			$moduleHeight = element.outerHeight(true);
		//		});
		messageModule.css('height',$moduleHeight+'px').stop().slideDown(speed,method);
	}
};




/* ---------------------------------------------- CLONE ------------------------------------------------- */
tiaacref.clone = function (obj) {
	"use strict";
	if (typeof obj === 'undefined') {
		return null;
	} else if (obj instanceof Array) {
		return $.merge([], obj);
	}
	return $.extend(true, {}, obj);
};






/* ------------------------------------------------- CREATEID --------------------------------------------- */
// TODO: Delete after removing the old $tc function from portal.js
tiaacref.createId = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.createId;
	if (typeof arg !== 'string') {
		arg = '';
	}
	defaults.idCount += 1;
	return arg + defaults.name + defaults.idCount;
};








/* ---------------------------------------------- IsNumber ------------------------------------------- */
tiaacref.isNumber = function (arg) {
	"use strict";
	return !isNaN(parseFloat(arg)) && isFinite(arg);
};


/* ---------------------------------------------- Check Number ----------------------------------------- */
tiaacref.checkNumber = function (n) {
	"use strict";
	if (!tiaacref.isNumber(n)) {
		return Number.NaN;
	}
	return n;
};


/* ----------------------------------------------- apply Menu Analytics ---------------------------------- */
tiaacref.applyMenuAnalytics = function (object, titleText) {
	"use strict";
	return;
/*	
 * removed in favor of global link setting for menu and megamenu.
 * 
	if (!tiaacref.useEnsighten) {
		var linkName = s_omtr.pageName + '-Header:' + titleText;
		s_omtr.prop34 = 'Menu:' + s_omtr.channel + ':' + titleText;
		s_omtr.tl(object, 'o', linkName);
	}
	return;
*/	
};


/* ------------------------------------------------- Megamenu repository ----------------------------------- */
tiaacref.megamenuData = {
	element: '.l1nav',
	align: 'right',
	fill: true
};

/*
 * =================================================== INDEPENDENT ==================================
 * 
 *  This variable controls when to release any tooltip, dropdown, and any other element
 *  that operates as an "independent" element.
 *  
 */
tiaacref.independentElement = function () {
	"use strict";
	this.active = 0;
	this.list = [];
	this.NOFIRECHECK = 0;
	this.STILLINANCHOR = 1;
	this.STILLININDEPENDENT = 2;
	this.LEFTINDEPENDENT = 3;
	this.LEFTANCHOR = 4;
	this.ERASEOTHERS = 5;
	this.LEFTANCHORIMMEDIATE = 6;
};
tiaacref.independentElement.prototype.setActive = function (num) {
	"use strict";
	if (typeof num !== 'number') {
		return;
	}
	this.active = num;
	return;
};
tiaacref.independentElement.prototype.getActive = function () {
	"use strict";
	return this.active;
};
tiaacref.independentElement.prototype.initiate = function (timeout) {
	"use strict";
	if (typeof timeout !== 'number') {
		return;
	}
	setTimeout('tcoIndependent.operate(' + timeout + ')', timeout);
	return;
};
tiaacref.independentElement.prototype.operate = function (timeout) {
	"use strict";
	var listItem,
		item;
	if (typeof timeout !== 'number') {
		return;
	}
	var fireTimeout = true,
		listLength = this.list.length,
		i;
	switch (this.active) {
	case this.NOFIRECHECK: // 0, we do nothing. Do not fire check
		fireTimeout = false;
		break;
	case this.STILLINANCHOR: //1, Independent active, still in anchor. Do nothing. Fire check
		break;
	case this.STILLININDEPENDENT: //2, Independent active, inside independent. Do nothing. Fire check
		break;
	case this.LEFTINDEPENDENT: //3, Independent not active, left independent. Clear active, do not fire check
		this.active = this.NOFIRECHECK;
		fireTimeout = false;
		break;
	case this.LEFTANCHOR: //4, Indpendent has left the anchor. Set to 5 to trigger a clear. Fire check
		this.active = this.ERASEOTHERS;
		break;
	case this.ERASEOTHERS: //5, Independent is still active. Erase all other independents, reset counters
		for (i = 0; i < listLength; i++) {
			item = this.list[i];
			if (item.attr('data-sticky') !== 'true') {
				item.addClass('hidden');
				item.css({display: ''});
				item.attr({'aria-hidden': true});
//				item.parents('div.ui-dialog').first().removeClass('ovisible');
//				item.parents('div.messageBodyContent').first().parent().removeClass('ovisible');
			}
		}
		this.active = this.NOFIRECHECK;
		fireTimeout = false;
		break;
	default:
		break;
	}
	if (fireTimeout) {
		setTimeout('tcoIndependent.operate(' + timeout + ')', timeout);
	}
};
tiaacref.independentElement.prototype.clear = function () {
	"use strict";
	var listLength = this.list.length,
		i;
	for (i = 0; i < listLength; i++) {
		if (this.list[i].attr('data-sticky') !== 'true') {
// 			this.list[i].parents('div.ui-dialog').first().removeClass('.ovisible');;
//			this.list[i].parents('div.messageBodyContent').first().parent().removeClass('.ovisible');
			this.list[i].addClass('hidden').css({display: ''});
		}
	}
	return;
};
tiaacref.independentElement.prototype.add = function (item, type) {
	"use strict";
	var defaults = tiaacref.defaults.independentElement;
	if (typeof type !== 'string') {
		type = defaults.type;
	}
	if (typeof defaults.validType[type] === 'undefined') {
		type = defaults.type;
	}
	this.list.push(item);
	return;
};
var tcoIndependent = new tiaacref.independentElement();



/* ----------------------------------------------- HTML CLICK OUTSIDE -------------------------- */
tiaacref.clickOutside = function () {
	"use strict";
	this.database = [];
	this.assignedClick = false;
	return;
};
tiaacref.clickOutside.prototype.add = function (element) {
	this.database.push(element);
	if (!this.assignedClick) {
		$('html').click({self: this}, function(e) {
			"use strict";
			e.stopPropagation();
			e.data.self.clear();
		});
	}
	return;
};
tiaacref.clickOutside.prototype.clear = function () {
	"use strict";
	var i,
		l = this.database.length;
	for (i = 0; i < l; i++) {
		this.database[i].addClass('hidden');
	}
	this.database = [];
	return;
};
var tcoClickOutside = new tiaacref.clickOutside();



/* ================================================== IS IE ================================================ */
tiaacref.isIE = function () {
	"use strict";
	var def = tiaacref.defaults.isIE,
		h = $('html');
	if (def.ie === null) {
		def = {
			ie: false,
			ie6: h.hasClass('ie6'),
			ie7: h.hasClass('ie7'),
			ie8: h.hasClass('ie8')
		};
		def.ie = def.ie6 | def.ie7 | def.ie8;
	}
	return def;
};



/* ========================================== SUPPORTED BROWSER ==================================== */
tiaacref.supportedBrowser = function () {
	"use strict";
	return $("html").hasClass("ie8") || ($("html").hasClass("opacity") && $("html").hasClass("canvas"));
};




/*
 * =================================================== DEFERRED ======================================
 */
tiaacref.deferredProcessing = function () {
	"use strict";
	this.parent = null;
	return;
};
tiaacref.deferredProcessing.prototype.add = function (element) {
	"use strict";
	if (this.parent === null) {
		this.parent = $('<div>').addClass('tcDeferred').attr({id: 'tcDeferred'});
		$(document).ready(function () {
			$('body').append(tcoDeferred.parent);
		});
	}
	this.parent.append(element);
};
var tcoDeferred = new tiaacref.deferredProcessing();










/* ==================================================== IPC ================================================== */

tiaacref.interPortletCommunications = function () {
	"use strict";
	this.events = {};
	this.eventCount = 0;
};
tiaacref.interPortletCommunications.prototype.fire = function (event, payload, name) {
	"use strict";
	var allEvents = this.events,
		e,
		theEvent,
		event;
	if (typeof event !== 'string') {
		throw new tiaacref.typeError("Argument 'event' must be present and be a string.");
	}
	if (typeof payload === 'undefined') {
		throw new tiaacref.missingArgument("Argument 'payload' is missing.");
	}
	if (typeof name === 'undefined') {
		name = null;
	}
	if (typeof allEvents[event] === 'undefined') {
		return;	// now, just consume the event. Do not throw an error
	}
	theEvent = allEvents[event];
	for (e in theEvent) {
		if (theEvent.hasOwnProperty(e)) {
			try {
				event = theEvent[e];
				if (event.name == name) {
					if (event.extraData === null) {
						event.action(payload);
				} else {
						event.action(payload, event.extraData);
				}
				}
			} catch (ipcError) {
				if (ipcError instanceof Error) {
					throw tiaacref.error('', 'Thrown error in component: ' + ipcError.message);
				} else {
					throw tiaacref.error('', 'Thrown error in event error: ' + ipcError.number + ' - ' + ipcError.description);
				}
			}
		}
	}
	return true;
};
tiaacref.interPortletCommunications.prototype.on = function (event, ipcFunction, extraData, name) {
	"use strict";
	if (typeof event !== 'string') {
		throw tiaacref.error('badId', '', 'Missing event argument for IPC.on');
	}
	if (typeof ipcFunction !== 'function') {
		throw tiaacref.error('badId', '', 'Missing function argument for IPC.on');
	}
	if (typeof extraData === 'undefined') {
		extraData = null;
	}
	if (typeof name === 'undefined') {
		name = null;
	}
	var allEvents = this.events,
		eventCount = this.eventCount;
	if (typeof allEvents[event] === 'undefined') {
		allEvents[event] = [];
		eventCount += 1;
	}
	allEvents[event].push({action: ipcFunction, extraData: extraData, name: name});
	return true;
};
var tcoipc = new tiaacref.interPortletCommunications();








/* ============================================================= FORMAT ============================================ 
 * 
 *	format values
 *		'$' - print as currency (default 0.2). Prints comma
 *		'#' - print as number (default 0.2). Prints comma
 *		'%' - print as percent (default 0.2). Prints comma
 *		'mM,dD,yY' - print as a date
 *		number.number - pint as a decimal number
 */
tiaacref.format = function (value, format) {
	"use strict";
	var defaults = tiaacref.defaults.format,
		output = '',
		type,
		extraFormat,
		integer,
		decimal,
		fullValue,
		sa,
		fl,
		foundDecimal,
		integerCount,
		i,
		comma,
		ft,
		fValue;
	if (typeof value === 'undefined') {
		return '';
	}
	if (typeof format !== 'string') {
		format = defaults.standard;
	}
	if (format === '') {
		return value;
	}
	format = (format.length === 1 ? format + defaults.standard : format);
	type = format.substr(0,1);
	extraFormat = format.substr(1);
	integer = null;
	decimal = null;
	fullValue = null;
	if (tiaacref.isNumber(type) || ("$#%".indexOf(type) !== -1)) {
		if (!tiaacref.isNumber(value)) {
			return '';
		}
		fullValue = new Number(value);
		if (type === '%') {
			fullValue *= 100;
		}
		if (tiaacref.isNumber(type)) {
			extraFormat = format;
		}
		extraFormat = (tiaacref.isNumber(extraFormat) ? extraFormat : defaults.standard);
		sa = extraFormat.split('.');
		if (sa.length === 1) {
			integer = new Number(parseInt(sa[0]));
			decimal = new Number(0);
		} else {
			integer = new Number(parseInt(sa[0]));
			decimal = new Number(parseInt(sa[1]));
		}
		fValue = (Math.round(fullValue * (Math.pow(10, decimal))) / (Math.pow(10, decimal))).toFixed(decimal);
		if ('#$%'.indexOf(type) !== -1) {
			if (type === '$') {
				fValue = '$' + fValue;
			}
			fl = fValue.length - 1;
			foundDecimal = false;
			integerCount = 0;
			for (i = fl; i >= 0; i--) {
				if (decimal > 0 && !foundDecimal) {
					output = fValue.substr(i,1) + output;
					if (fValue.substr(i,1) === '.') {
						foundDecimal=true;
					}
				} else {
					output = fValue.substr(i,1) + output;
					integerCount += 1;
					if (integerCount % 3 === 0) {
						output = ',' + output;
					}
				}
			}
			if (output.substr(0,1) === ',') { 
				output = output.substring(1);
			} else if (output.substr(0,2) === '$,') {
				output = '$' + output.substring(2);
			} else if (output.substr(0,2) === '-,') {
				output = '-' + output.substring(2);
			} else if (output.substr(0,3) === '$-,') {
				output = '-$' + output.substring(3);
			} else if (output.substr(0,2) === '$-') {
				output = '-$' + output.substring(2);
			}
		} else {
			output += fValue;
		}
		if (integer != 0) {
			comma = output.indexOf('.');
			if (comma === -1) {
				comma = output.length;
			}
			if (comma > integer) {
				output = '*' + output.substr((comma - integer), integer) + output.substr(comma);
			}
		}
		if (type === '%') {
			output += '%';
		}
	} else if ('mMdDyY'.indexOf(type) !== -1 && value instanceof Date) {
		fl = format.length;
		for (i = 0; i < fl; i++) {
			ft = format.substr(i, 1);
			if (ft === 'm') {
				if (value.getMonth() + 1 < 10) output += '0';
				output += value.getMonth() + 1;
			} else if (ft === 'M') {
				output += defaults.months.shortName[value.getMonth()];
			} else if (ft === 'd') {
				output += value.getDate();
			} else if (ft == 'D') {
				if (value.getDate() < 10) {
					output += '0';
				}
				output += value.getDate();
			} else if (ft === 'y' || ft === 'Y') {
				output += value.getFullYear();
			} else {
				output += ft;
			}
		}
	}
	return output;
};



/* ============================================== NEXT COLOR ================================================= */
tiaacref.colorAssignments=new Array();
tiaacref.colorCount=0;
tiaacref.nextColor = function(key) {
	"use strict";
	var defaults = tiaacref.defaults.nextColor,
		newKey;
	if (typeof key !== 'string') {
		key = '';
	}
	if (key === '' && defaults.colorBlank) {
		key = 'blank';
	}
	if (key !== '') {
		newKey = key.replace(' ', '');
		if (typeof tiaacref.colorAssignments[newKey] === 'undefined') {
			if (tiaacref.colorCount >= defaults.colorList.length) {
				tiaacref.colorCount = 0;
			}
			tiaacref.colorAssignments[newKey] = defaults.colorList[tiaacref.colorCount++];
		}
		return tiaacref.colorAssignments[newKey];
	} else {
		if (tiaacref.colorCount >= defaults.colorList.length) {
			tiaacref.colorCount = 0;
		}
		return defaults.colorList[tiaacref.colorCount++];
	}
};


/* ---------------------------------------------- SerializeDate ------------------------------------- */
tiaacref.serializeDate = function (theDate, includeTime) {
	"use strict";
	var output = '',
		temp = '';
	if (typeof includeTime !== 'boolean') {
		includeTime = false;
	}
	if (!theDate instanceof Date) {
		throw new tiaacref.Error('', 'serializeDate argument not a Date object.');
	}
	output = theDate.getFullYear().toString();
	temp = theDate.getMonth().toString();
	if (temp.length === 1) {
		temp = '0' + temp;
	}
	output += '&' + temp;
	temp = theDate.getDate().toString();
	if (temp.length === 1) {
		temp = '0' + temp;
	}
	output += '&' + temp;
	if (includeTime) {
		temp = theDate.getHours().toString();
		if (temp.length === 1) {
			temp = '0' + temp;
		}
		output += '&' + temp;
		temp = theDate.getMinutes().toString();
		if (temp.length === 1) {
			temp = '0' + temp;
		}
		output += '&' + temp;
		temp = theDate.getSeconds().toString();
		if (temp.length === 1) {
			temp = '0' + temp;
		}
		output += '&' + temp;
	}
	return output.replace(/&/gi, '');
};

/* ---------------------------------------------- Valid Date ------------------------------ */
tiaacref.isValidDate = function (theDate) {
	if (Object.prototype.toString.call(theDate) !== "[object Date]" ) {
		return false;
	}
	return !isNaN(theDate.getTime());
};


/* ---------------------------------------------- Parse Comma List --------------------------- */
tiaacref.parseCommaList = function (list) {
	"use strict";
	var i = 0,				
		returnArray = [],
		j = 0,
		l = 0,
		quoteDelimiter = '',
		oldQuoteDelimiter = '',
		char = '';				
	if (typeof list !== 'string') {
		throw tiaacref.error('', 'Argument not a string in parseCommaList');
	}
	l = list.length;
	while (j < l) {
		char = list.substr(j,1);
		if (char === "'" || char === '"') {
			if (quoteDelimiter === char) {
				oldQuoteDelimiter = quoteDelimiter;
				quoteDelimiter = "";
				j ++;
			} else {
				oldQuoteDelimiter = "";
				quoteDelimiter = char;
				j ++;
				i = j;
			}
		} else if(char === ',') {
			if (quoteDelimiter === '') {
				returnArray.push(list.substring(i, j).replace(oldQuoteDelimiter, ''));
				i = j + 1;
			}
			j ++;
		} else {
			j ++;
		}
	}
	if (i != j) {
		returnArray.push(list.substring(i).replace("'","").replace('"',""));
	}
	return returnArray;
};








/* ----------------------------------------------- serverEnvironment() ------------------------------------- */
tiaacref.serverEnvironment = function() {
	var defaults = tiaacref.defaults.serverEnvironment,
		domainName = document.domain,
		analyticsTag = defaults.analyticsTags['default'],
		gomezTag = defaults.gomezTags['default'],
		serverName = (typeof defaults.serverList[domainName] === 'undefined' ? '' : defaults.serverList[domainName]);
	return {
		domain: domainName,
		analyticsTag: (serverName === '' ? analyticsTag : defaults.analyticsTags[serverName]),
		gomezTag:  (serverName === '' ? gomezTag : defaults.gomezTags[serverName])
	};
};







/* ------------------------------------------------ pleaseWait() ------------------------------------------- */
tiaacref.pleaseWaitParameters = {
	active: false,
	text: '',
	icon: '/images/wait.gif',
	jq: {
		body: null,
		pleaseWaitMask: null
	}
};
tiaacref.pleaseWait = {
	start: function() {
		var pw = tiaacref.pleaseWaitParameters;
		if (pw.jq.body == null) {
			tiaacref.pleaseWait.initialize();
		}
		pw.jq.body.addClass('loading').css({overflow: 'hidden'});
		pw.jq.pleaseWaitMask.removeClass('hidden').attr({'aria-hidden': false});
		tiaacref.pleaseWaitParameters.active = true;
	},
	stop: function() {
		var pw = tiaacref.pleaseWaitParameters;
		tiaacref.pleaseWaitParameters.active = false;
		if (pw.jq.pleaseWaitMask != null) {
			pw.jq.pleaseWaitMask.addClass('hidden').attr({'aria-hidden': true});
		}
		if (pw.jq.body != null) {
			pw.jq.body.removeClass('loading').css({overflow: ''});
		}
	},
	initialize: function() {
		var pw = tiaacref.pleaseWaitParameters;
		if (pw.jq.body == null) {
			tiaacref.pleaseWaitParameters.jq.body = $('body');
			tiaacref.pleaseWaitParameters.jq.body.append('<div id="pagemask" class="pagemask"></div>');
			tiaacref.pleaseWaitParameters.jq.pleaseWaitMask = $('#pagemask');
			pw.jq.pleaseWaitMask
				.addClass('hidden')
				.attr({'aria-hidden': true})
				.css({
					position: 'fixed', 
					'z-index': 99999, 
					display: 'block',
					'background-color': 'white',
					'background-position': '50% 50%',
					'background-repeat': 'no-repeat',
					'background-image': "url('" + tiaacref.mediaServer + pw.icon + "')"
				});
			pw.jq.body.removeClass('loading').css({overflow: ''});
		}
	},
	setIcon: function(icon) {
		tiaacref.pleaseWaitParameters.icon = icon;
		return;
	}
};











/* ===================================================== TOOLTIP =============================================== */
tiaacref.tooltip = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.tooltip,
		jq,
		nid,
		tootipParent,
		rightcol,
		tabIndex;
	if (typeof arg !== 'object') {
		arg = {
			icon: defaults.icon,
			text: defaults.text,
			id: defaults.id,
			detached: defaults.detached,
			tooltip: defaults.tooltip,
			size: defaults.size,
			position: defaults.position,
			attachTo: defaults.attachTo,
			inline: defaults.inline,
			onClick: defaults.onClick,
			style: defaults.style
		};
	} else {
		if (typeof arg.icon !== 'string') {
			arg.icon = defaults.icon;
		}
		if (typeof arg.text !== 'string') {
			arg.text = defaults.text;
		}
		if (typeof arg.detached !== 'boolean') {
			arg.detached = defaults.detached;
		}
		if (typeof arg.onclick !== 'function') {
			arg.onClick = defaults.onClick;
		}
		if (typeof arg.size !== 'string' && typeof arg.size !== 'object') {
			arg.size = defaults.size;
		}		
		if (typeof arg.onClick !== 'function') {
			arg.onClick = defaults.onClick;
		}
		if (typeof arg.position !== 'string') {
			arg.position = defaults.position;
		}
		if (typeof defaults.positionValues[arg.position] === 'undefined') {
			arg.position = defaults.position;
		}

		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.tooltip !== 'string') {
			arg.tooltip = defaults.tooltip;
		}
		if (typeof arg.attachTo !== 'string') {
			arg.attachTo = defaults.attachTo;
		}
		if (typeof arg.inline !== 'boolean') {
			arg.inline = defaults.inline;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof defaults.validStyle[arg.style] === 'undefined') {
			arg.style = defaults.style;
		}
	}


	// check for legal values
	jq = ({
		id: $(arg.id), 
		tooltip: $(arg.tooltip),
		attachTo: null
	});
	if (arg.detached === false && arg.attachTo === "") {
		if (jq.id.length === 0) {
			throw tiaacref.error('badId', arg.id, 'tooltip');
		}
	}
	if (jq.tooltip.length === 0) {
		throw tiaacref.error('badId', arg.tooltip, 'tooltip');
	}
	arg.customSize = tiaacref.normalizeSize(arg.size, defaults);
	this.arg = arg;
	this.jq = jq;
	this.focusableElementBlur = true;
	this.tooltip = {
		content: this.jq.tooltip.find('div.bd').first()
	};
	this.focusableElements = this.tooltip.content.find('input,a,button,map,select,textarea,[tabindex]');
//	if (arg.size !== 'auto') {
//		jq.tooltip.width(defaults.validSize[arg.size]);
//	}
	jq.tooltip.width(arg.customSize.width);
	if (arg.attachTo !== "") {
		this.jq.attachTo = $(arg.attachTo);
		if (this.jq.attachTo.length === 0) {
			throw tiaacref.missingDOM('badId', arg.id, 'tooltip');
		}
		tabIndex = this.jq.attachTo.attr('tabindex');
		if (typeof tabIndex === 'undefined') {
			this.jq.attachTo.attr({tabindex: 0});
			this.jq.attachTo.css({cursor: 'pointer'});
		}
		arg.id = arg.attachTo;
		this.jq.id = $(arg.id);
	}
	if (this.arg.inline === false) {
		tcoDeferred.add(jq.tooltip);
	}
	// sticky setting
	jq.tooltip.attr({'data-sticky': (arg.style === 'sticky' ? true : false)});
	
	tcoIndependent.add(jq.tooltip);
	// IE7 fix for new layout
	
	if (tiaacref.isIE().ie7) {
		tooltipParent = this.jq.tooltip.parent();
		if (tooltipParent.length > 0) {
			tooltipParent = tooltipParent.parent();
			if (tooltipParent.length > 0) {
				tooltipParent = tooltipParent.parent();
				if (tooltipParent.hasClass('hd')) {
					tooltipParent.css({position: 'static'});
				}
			}
		}
	}
	
	//
	// mouse in/out options TODO: add support for touch devices
	//
	if (arg.detached === false) {
		this.detached = true;
		this.attachTo(arg.id);
		this.detached = false;
	}
};
tiaacref.tooltip.prototype.getId = function () {
	"use strict";
	if (this.arg.style === 'hidden') {
		return null;
	}
	return this.arg.id;
};
tiaacref.tooltip.prototype.getTooltipId = function () {
	"use strict";
	return this.arg.tooltip;
};
tiaacref.tooltip.prototype.clear = function () {
	"use strict";
	this.tooltip.content.empty();
	return;
};
tiaacref.tooltip.prototype.replace = function (arg) {
	"use strict";
	this.clear(false);
	this.tooltip.content.append(arg);
	return;
};
tiaacref.tooltip.prototype.attachTo = function (id) {
	"use strict";
	var defaults = tiaacref.defaults.tooltip,
		jq = this.jq,
		arg = this.arg,
		jqId,
		self;
	if (typeof id !== 'string') {
		throw tiaacref.error('badId', id, 'tooltip.attachTo()');
	}
	if (!this.detached) {
		this.detach();
	}
	jqId = $(id);
	if (jqId.length === 0) {
		throw tiaacref.error('badId', id, 'tooltip.attachTo()');
	}
	this.jq.id = jqId;
	this.arg.id = id;
	self = this;
	if (this.arg.style === 'modal' || this.arg.style === 'sticky') {
		this.jq.id.on('click', {self: this}, function(event) {
			var self = event.data.self;
			self.show();
		});
	} else {
		this.jq.id.on('focusin', {self: this}, function(event) {
			var self = event.data.self;
			self.show();
		});
		this.jq.id.on('focusout', {self: this}, function(event) {
			var self = event.data.self;
			if (self.focusableElements.length === 0) {
				self.close();
			} else {
				tcoIndependent.setActive(tcoIndependent.LEFTANCHOR);
			}
		});
	}
	
	this.focusableElements.on('focusout focusin', {self: this}, function(event) {
		"use strict";
//console.log(event);		
		switch(event.type) {
		case 'focusout':
			tcoIndependent.setActive(tcoIndependent.LEFTANCHOR);
			break;
		case 'focusin':
			tcoIndependent.setActive(tcoIndependent.STILLININDEPENDENT);
			break;
		}
	});
	
	if (this.arg.style !== 'modal' || this.arg.style === 'sticky') {
		jq.id.mouseleave({self: this}, function (event) {
			var self = event.data.self;
			tcoIndependent.setActive(tcoIndependent.LEFTANCHOR);
			tcoIndependent.operate(tiaacref.defaults.independentElement.timeout);
		});
		jq.id.mouseenter({self: this}, function (event) {
			var self = event.data.self,
				id = event.data.self.jq.id,
				tt = event.data.self.jq.tooltip,
				px = id.offset().left + id.outerWidth(true),
				py;
			self.show();
			tcoIndependent.setActive(tcoIndependent.STILLINANCHOR);
			tt.mouseleave({self: self}, function (ee) {
//				$(this).parents('div.ui-dialog').first().removeClass('ovisible');
//				$(this).parents('div.messageBodyContent').first().parent().removeClass('ovisible');
				$(this).addClass('hidden').css({display: ''}).attr({'aria-hidden': true});
				tcoIndependent.setActive(tcoIndependent.LEFTINDEPENDENT);
			});
			tt.mouseenter({self: self}, function (ee) {
				tcoIndependent.setActive(tcoIndependent.STILLININDEPENDENT);
			});
			return;
		});
	}
	
	
	function tcTooltipFocusableElementTestBlur() {
		if (self.getFocusableElementBlur() === true) {
			self.close();
		}
	}
	return;
};
tiaacref.tooltip.prototype.setFocusableElementBlur = function(setting) {
	"use strict";
	this.focusableElementBlur = setting;
};
tiaacref.tooltip.prototype.getFocusableElementBlur = function() {
	"use strict";
	return this.focusableElementBlur;
};
tiaacref.tooltip.prototype.close = function() {
	"use strict";
	tcoIndependent.clear();
	tcoIndependent.setActive(tcoIndependent.LEFTANCHOR);
//	this.jq.tooltip.parents('div.ui-dialog').first().removeClass('ovisible');
//	this.jq.tooltip.parents('div.messageBodyContent').first().parent().removeClass('ovisible');
	this.jq.tooltip.addClass('hidden').css({display: ''}).attr({'aria-hidden': true});
};
tiaacref.tooltip.prototype.show = function() {
	"use strict";
	var self = this,
		id = self.jq.id,
		tt = self.jq.tooltip,
		px,
		py,
		rightcol,
		positioningComplete = false,
		originalPosition = self.arg.position,
		position,
		boundaryPosition,
		overflowPosition,
		ttWidth,
		ttHeight,
		ttLeft,
		ttTop,
		ttPointer = tt.children('.pointer'),
		numberPositionChanges = 0,
		windowHeight = $(window).height(),
		windowWidth = $(window).width(),
		windowLeft = $(window).scrollLeft(),
		windowTop = $(window).scrollTop(),
		dialogParent = tt.parents('div.ui-dialog').first(),
		inDialog = dialogParent.length === 1 ? true : false,
		messageParent = tt.parents('div.messageBodyContent').first().parent(),
		inMessage = messageParent.length === 1 ? true : false;
	tcoIndependent.clear();
	tcoIndependent.setActive(tcoIndependent.STILLINANCHOR);
	tcoIndependent.operate(tiaacref.defaults.independentElement.shorterTimeout);
//	if (inDialog) {
//		dialogParent.addClass('ovisible');
//	}
//	if (inMessage) {
//		messageParent.addClass('ovisible');
//	}
	tt.css({position: 'absolute', top: 0, left: 0, zIndex: 1010, display: 'block'})
		.removeClass('hidden')
		.attr({'aria-hidden': false});
	while (positioningComplete === false && numberPositionChanges < 3) {
		position = tiaacref.defaults.tooltip.positionValues[self.arg.position];
		tt.removeClass('pointerRight pointerLeft pointerTop pointerBottom').addClass(position);
		ttWidth = tt.outerWidth(true);
		ttHeight = tt.outerHeight(true);
		switch(self.arg.position) {
		case 'left':
			ttWidth = tt.outerWidth(true);
			ttHeight = tt.outerHeight(true);
			if (self.arg.inline === true) {
				px = id.position().left - ttWidth;
				py = id.position().top - (ttHeight / 2);
			} else {
				px = id.offset().left - ttWidth;
				py = id.offset().top - (ttHeight / 2);
			}
			tt.css({top: py, left: px, position: 'absolute'});
			ttPointer.css({top: (ttHeight / 2)});
			ttLeft = tt.offset().left;
			ttTop = tt.offset().top;
			boundaryPosition = (inDialog ? dialogParent.offset().left : windowLeft);
			if (tt.offset().left < boundaryPosition) {
				self.arg.position = 'right';
				numberPositionChanges ++;
			} else {
				positioningComplete = true;
			}
			fixPosition('y');
			break;
		case 'top':
			ttWidth = tt.outerWidth(true);
			ttHeight = tt.outerHeight(true);
			if (self.arg.inline === true) {
				px = id.position().left - (ttWidth / 2) + 20;
				py = id.position().top - ttHeight - 5;
			} else {
				px = id.offset().left - (ttWidth / 2) + 20;
				py = id.offset().top - ttHeight - 5;
			}
			tt.css({top: py, left: px, position: 'absolute'});
			ttPointer.css({left: (ttWidth / 2) - ttPointer.outerWidth(true)});
			ttLeft = tt.offset().left;
			ttTop = tt.offset().top;
			boundaryPosition = (inDialog ? dialogParent.offset().top : windowTop);
			if (tt.offset().top < boundaryPosition) {
				self.arg.position = 'bottom';
				numberPositionChanges ++;
			} else {
				positioningComplete = true;
			}
			fixPosition('x');
			break;
		case 'bottom':
			if (self.arg.inline === true) {
				px = id.position().left - (ttWidth / 2) + 20;
				py = id.position().top + id.outerHeight(true) + 5;
			} else {
				px = id.offset().left - (ttWidth / 2) + 20;
				py = id.offset().top + id.outerHeight(true) + 5;
			}
			tt.css({top: py, left: px, position: 'absolute'});
			ttPointer.css({left: (ttWidth / 2) - ttPointer.outerWidth(true)});
			ttLeft = tt.offset().left;
			ttTop = tt.offset().top;
			boundaryPosition = (inDialog ? dialogParent.offset().top + dialogParent.outerHeight(true) : windowHeight + windowTop);
			if (ttTop + ttHeight > boundaryPosition) {
				self.arg.position = 'top';
				numberPositionChanges ++;
			} else {
				positioningComplete = true;
			}
			fixPosition('x');
			break;
		case 'right':
		default:
			if (self.arg.inline === false) {
				py = id.offset().top + (id.outerHeight(true) / 2) - (ttHeight / 2);
				px = id.offset().left + id.outerWidth(true);
			} else {
				py = id.position().top - (ttHeight / 2);
				px = id.position().left + id.outerWidth(true);
			}
			tt.css({top: py, left: px, position: 'absolute'});
			ttPointer.css({top: (ttHeight / 2)});
			ttLeft = tt.offset().left;
			ttTop = tt.offset().top;
			boundaryPosition = (inDialog ? dialogParent.offset().left + dialogParent.outerWidth(true) : windowWidth + windowLeft);
			if (tt.offset().left + ttWidth > boundaryPosition) {
				self.arg.position = 'left';
				numberPositionChanges ++;
			} else {
				positioningComplete = true;
			}
			fixPosition('y');
			break;
		}
		
	}
	self.arg.position = originalPosition;
	if (tiaacref.isIE().ie7) {
		rightcol = $('section.rightcol');
		if (rightcol.length > 0) {
			rightcol.css({'z-index': -1});
		}
	}
	return;
	function fixPosition(axis) {
		var left,
			width,
			top,
			height,
			dialogContent;
		if (inDialog) {
			dialogContent = dialogParent.find('.body').first();
			width = dialogContent.outerWidth(true);
			left = dialogContent.offset().left;
			height = dialogContent.outerHeight(true);
			top = dialogContent.offset().top;
		} else {
			width = windowWidth;
			left = windowLeft;
			height = windowHeight;
			top = windowTop;
		}
		if (axis === 'x') {
			if (ttWidth < width) {
				if (ttLeft < left) {
					tt.css({left: px + (left - ttLeft)});
					ttPointer.css({left: ttPointer.position().left - (left - ttLeft)});
					
				} else if (ttLeft + ttWidth > left + width) {
					tt.css({left: px - ( (ttLeft + ttWidth) - (left + width) )});
					ttPointer.css({left: ttPointer.position().left + ( (ttLeft + ttWidth) - (left + width) )});
				}
			}
		} else if (axis === 'y') {
			if (ttHeight < height) {
				if (ttTop < top) {
					tt.css({top: py + (top - ttTop)});
					ttPointer.css({top: ttPointer.position().top - (top - ttTop)});
					
				} else if (ttTop + ttHeight > top + height) {
					tt.css({top: py - ( (ttTop + ttHeight) - (top + height) )});
					ttPointer.css({top: ttPointer.position().top + ( (ttTop + ttHeight) - (top + height) )});
				}
			}
		}
		return;
	}
};

tiaacref.tooltip.prototype.detach = function () {
	"use strict";
	var defaults = tiaacref.defaults.tooltip,
		jq = this.jq,
		arg = this.arg;
	if (arg.detached === true) {
		return;
	}
	jq.id.unbind('mouseleave mouseenter');
	this.detached = true;
	return;
};
tiaacref.tooltip.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('tooltip.' + eventName) + this.arg.id;
};

tiaacref.produceTooltipHtml = function (text, type) {
	"use strict";
	var newText = text,
		newId = tiaacref.createId();
	if (typeof text !== 'string') {
		throw tiaacref.error('', 'First argument in produceHtml not a string');
	}
	if (typeof type !== 'string') {
		throw tiaacref.error('', 'Second argument in produceHtml not a string');
	}
	switch(type) {
	case 'icon':
		newText = 
			'<input type="button" ' +
			'id="' + newId + '" ' +
			'name="' + newId + '" ' +
			'class="tipLink rel" ' +
			'value="?" ' +
			'role="button" ' +
			'aria-describedby="tooltip' + newId + '" ' +
			'aria-required="false" ' +
			'title="" />';
		break;
	case 'text':
		newText = 
			'<input type="button" ' +
			'id="' + newId + '" ' +
			'name="' + newId + '" ' +
			'class="tipLink2 rel" ' +
			'value="' + text + '" ' +
			'role="button" ' +
			'aria-describedby="tooltip' + newId + '" ' +
			'aria-required="false" ' +
			'title="" />';
		break;
	default:
		throw tiaacref.error('', 'Second argument in produceHtml must be "text" or "icon".');
		break;
	}
	newText += 	
		'<div id="tooltip' + newId + '" class="infoHover pointer" role="tooltip" aria-hidden="true">' +
		'<div class="pointer"></div>' + 
		'<div class="bd txt txtn txtncase" style="white-space: normal; border: 1px solid #D6D6D6 !important;">' +
		'@hover@' +
		'</div>' + 
		'</div>';
	return {id: newId, tooltipId: 'tooltip' + newId, html: newText};
};










/* ===================================================== POPUP ============================================ */
tiaacref.popup = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.popup,
		jq,
		nid,
		self,
		newWindowSetting,
		thisWindow;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			popup: defaults.popup,
			url: defaults.url,
			size: defaults.size,
			detached: defaults.detached,
			attachTo: defaults.attachTo,
			newWindow: defaults.newWindow,
			onClose: defaults.onClose,
			onOpen: defaults.onOpen
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.popup !== 'string') {
			arg.popup = defaults.popup;
		}
		if (typeof arg.url !== 'string') {
			arg.url = defaults.url;
		}
		if (typeof arg.size !== 'string' && typeof arg.size !== 'object') {
			arg.size = defaults.size;
		}
		if (typeof arg.detached !== 'boolean') {
			arg.detached = defaults.detached;
		}
		if (typeof arg.attachTo !== 'string') {
			arg.attachTo = defaults.attachTo;
		}
		if (typeof arg.newWindow !== 'object') {
			arg.newWindow = defaults.newWindow;
		} else {
			for(newWindowSetting in defaults.newWindow) {
				arg.newWindow[newWindowSetting] = typeof arg.newWindow[newWindowSetting] === 'undefined' 
					? defaults.newWindow[newWindowSetting] 
					: arg.newWindow[newWindowSetting];
				if (newWindowSetting !== 'name') {
					if (arg.newWindow[newWindowSetting] !== 'yes' && arg.newWindow[newWindowSetting] !== 'no') {
						arg.newWindow[newWindowSetting] = defaults.newWindow[newWindowSetting];
					}
				}
			}
		}
		if (typeof arg.onClose !== 'function') {
			arg.onClose = defaults.onClose;
		}
		if (typeof arg.onOpen !== 'function') {
			arg.onOpen = defaults.onOpen;
		}
	}
	arg.customSize = tiaacref.normalizeSize(arg.size, defaults);
	if (arg.attachTo !== '') {
		arg.id = arg.attachTo;
	}
	jq = {id: $(arg.id), popup: $(arg.popup)};
	if (arg.detached === false) {
		if (jq.id.length === 0) {
			throw tiaacref.error('badId', arg.id, 'popup');
		}
	}
	if (jq.popup.length === 0 && arg.url === '') {
		throw tiaacref.error('badId', arg.popup, 'popup', 'Also missing URL argument');
	}

	if (arg.newWindow.name === 'tcWindow') {
		arg.newWindow.name += arg.id;
	}
	/* IE fix for invalid characters in name */
	arg.newWindow.name = arg.newWindow.name.replace('#', '').replace('.', '');
	this.arg = arg;
	this.jq = jq;
	this.firedFromKeyboard = false;
	self = this;
	if (arg.detached === false && arg.url !== '') {
		jq.id.attr({href: arg.url, target: arg.newWindow.name});
		jq.id.on('click', function(event) {
			var widthHeight = '';
			if (self.arg.customSize.height !== 0) {
				widthHeight = "height=" + self.arg.customSize.height;
			}
			if (self.arg.customSize.width !== 0) {
				if (widthHeight !== "") {
					widthHeight += ",";
				}
				widthHeight += "width=" + self.arg.customSize.width;
			}
			if (widthHeight !== "") {
				widthHeight += ",";
			}
			var windowParameters =  
				widthHeight + 
				'directory=' + self.arg.newWindow.directory + 
				',location=' + self.arg.newWindow.location +
				',menubar=' + self.arg.newWindow.menubar + 
				',resizable=' + self.arg.newWindow.resizable +
				',status=' + self.arg.newWindow.status + 
				',toolbar=' + self.arg.newWindow.toolbar +
				',scrollbars=' + self.arg.newWindow.scrollbars;
			thisWindow = window.open(
				self.arg.url,
				self.arg.newWindow.name,
				windowParameters
			);
			try {
				thisWindow.focus();
			} catch (err) {
				//this case is for the IE6 issue where subsequent popup after a pdf popup has a JS error- member not found
				thisWindow.close();
				thisWindow = window.open(
					self.arg.url,
					self.arg.newWindow.name,
					windowParameters
				);
                thisWindow.focus();
			}
			return false;
		});
		return;
	}
	// build popup
	jq.popup.dialog({
		autoOpen: false,
		closeOnEscape: true,
		closeText: 'Close',
		dialogClass: 'modal',
		showClose: false,
		draggable: false,
		minHeight: 20,
		modal: true,
		open: function() {
			"use strict";
			if (tiaacref.isIE().ie6) {
				$('body select').css({visibility: 'hidden'});
				jq.popupDialog.find('select').css({visibility: 'visible'});
			}
			tcoipc.fire(self.event('open'), {});
			jq.popup.removeClass('hidden');
			arg.onOpen();
//console.log(jq.popup.offset());			
		},
		close: function() {
			"use strict";
			if (tiaacref.isIE().ie6) {
				$('body select').css({visibility: 'visible'});
			}
			arg.onClose();
			tcoipc.fire(self.event('close'), {});
			jq.id.focus();
//			if (self.getFiredFromKeyboard() === true) {
//				jq.id.focus();
//			}
		},
		resizable: false,
		width: arg.customSize.width
	});
	// additional prototype mods.
//	jq.popupDialog = $('[aria-labelledby="ui-dialog-title-' + jq.popup.attr('id') + '"]');
	jq.popupDialog = $('[aria-describedby="' + jq.popup.attr('id') + '"]');
	jq.popupDialog.attr({role: 'alertdialog'});
	jq.popupDialog.find('.ui-dialog-titlebar').addClass('head');
	jq.popupDialog.find('.ui-dialog-titlebar-close').addClass('popup-close').find('span.ui-icon').addClass('popup-close-icon');


	// on click
	if (arg.detached === false) {
		this.detached = true;
		this.attachTo(arg.id);
	}
	
};
tiaacref.popup.prototype.getId = function () {
	"use strict";
	if (this.arg.type !== 'hidden') {
		return this.arg.id;
	} else {
		return '';
	}
};
tiaacref.popup.prototype.getPopupId = function () {
	"use strict";
	return this.arg.popup;
};
tiaacref.popup.prototype.open = function () {
	"use strict";
	this.jq.popup.dialog('open');
	return;
};
tiaacref.popup.prototype.close = function () {
	"use strict";
	this.jq.popup.dialog('close');
	this.jq.id.focus();
	if (this.firedFromKeyboard === true) {
		this.jq.id.focus();
	}
	return;
};
tiaacref.popup.prototype.setFiredFromKeyboard = function (setting) {
	this.fireFromKeyboard = setting;
};
tiaacref.popup.prototype.getFiredFromKeyboard = function () {
	return this.fireFromKeyboard;
};

tiaacref.popup.prototype.attachTo = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.popup;
	if (typeof arg !== 'string') {
		throw tiaacref.error('', 'Argument not a string in popup');
	}
	if ($(arg).length === 0) {
		throw tiaacref.error('badId', arg, 'popup');
	}
	this.detach();
	this.arg.id = arg;
	this.jq.id = $(arg);
	this.jq.id.on('click', {self: this}, function (event) {
		var self = event.data.self,
			id = self.jq.id,
			popup = self.jq.popup,
			me = $(this);
		event.preventDefault();
//		if (event.keyCode === 13) {
//			self.setFiredFromKeyboard(true);
//		} else {
//			self.setFiredFromKeyboard(false);
//		}
		self.jq.id.blur();
		self.open();
		self.jq.popupDialog.find('.popup').removeClass('hidden');
		self.jq.popup.children().first().focus();
		return false;
	});
	this.detached = false;
	return;
};
tiaacref.popup.prototype.detach = function () {
	"use strict";
	var defaults = tiaacref.defaults.popup;
	if (this.detached === false) {
		this.jq.id.unbind('click');
	}
	this.detached = false;
	return;
};

tiaacref.popup.prototype.replaceContent = function (content) {
	this.jq.popupDialog.find('.body').empty().html(content);
	return;
};
tiaacref.popup.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('popup.' + eventName) + this.arg.id;
};




tiaacref.popup.prototype.resize = function (width, height) {
	var jq = this.jq;
	if (typeof width === 'undefined' && typeof height === 'undefined') {
//		console.log(this.jq.popupDialog.find('.body'));
	} else {
		if (typeof height === 'undefined') {
			height = 'auto';
		} else {
			height += 0;
		}
		if (typeof width === 'undefined') {
			width = 'auto';
		} else {
			width += 42;
		}
		jq.popup.dialog('option', 'width', width);
		jq.popup.dialog('option', 'height', height);
	}
};













/* ============================================== UTILITY LINK =============================================== */
tiaacref.utilityLink = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.utilityLink;
	return;
};














/* ============================================== BUTTON ================================================== */
tiaacref.button = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.button,
		jq;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			onClick: defaults.onClick
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.onClick !== 'function') {
			arg.onClick = defaults.onClick;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'button');
	}
	this.jq = jq;
	this.arg = arg;
	this.clickResult = null;
	this.jq.id.off('click.button.cl').on('click.button.cl', {self: this}, function(e) {
		"use strict";
		var self = e.data.self,
			retval = false;
		if (self.arg.type !== 'submit') {
			retval = true;
		}
		self.clickResult = self.arg.onClick();
		return retval;
	});
	return;
};
tiaacref.button.prototype.click = function () {
	"use strict";
	this.jq.id.click();
	return this.clickResult;
};
tiaacref.button.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};
tiaacref.button.prototype.getLastClickResult = function () {
	"use strict";
	return this.clickResult;
};
tiaacref.button.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('button.' + eventName) + this.arg.id;
};
tiaacref.button.prototype.disable = function () {
	this.jq.id.attr({disabled: 'disabled'});
	this.jq.id.addClass('btnOff');
};
tiaacref.button.prototype.enable = function () {
	this.jq.id.removeAttr('disabled');
	this.jq.id.removeClass('btnOff');
};







/* ============================================ CAROUSEL ============================================== */
tiaacref.carouselData = {};
tiaacref.carousel = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.carousel,
		jq,
		sections,
		adviceSections = null,
		i,
		l,
		ul,
		addClass,
		carouselADA,
		links,
		currentSlide,
		dataIndex,
		previousButton,
		nextButton,
		height,
		self;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			delay: defaults.delay,
			defaultDisplay: defaults.defaultDisplay,
			autoMove: defaults.autoMove,
			style: defaults.style,
			startPage: defaults.startPage,
			adviceContent: defaults.adviceContent,
			animationDuration: defaults.animationDuration,
			adviceCarouselCount: defaults.adviceCarouselCount
		};
	}
	else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.delay !== 'number') {
			arg.delay = defaults.delay;
		}
		if (typeof arg.defaultDisplay !== 'string') {
			arg.defaultDisplay = defaults.defaultDisplay;
		}
		if (typeof arg.autoMove !== 'boolean') {
			arg.autoMove = defaults.autoMove;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof defaults.validStyle[arg.style] === 'undefined') {
			arg.style = defaults.style;
		}
		if (typeof arg.adviceContent !== 'string') {
			arg.adviceContent = defaults.adviceContent;
		}
		if (typeof arg.animationDuration !== 'number') {
			arg.animationDuration = defaults.animationDuration;
		}
		if (typeof arg.adviceCarouselCount !== 'number') {
			arg.adviceCarouselCount = defaults.adviceCarouselCount;
		}
		if (arg.adviceCarouselCount <= 0) {
			arg.adviceCarouselCount = defaults.adviceCarouselCount;
		}
		if (typeof arg.startPage !== 'number') {
			arg.startPage = defaults.startPage;
	}
	}
	jq = {
		id: $(arg.id),
		adviceContent: $(arg.adviceContent),
		adviceContainer: $('#advicecontainer' + arg.id.substr(1)) 
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'carousel');
	}
	sections = (arg.style === 'simple' ? jq.id.find('ul').children('li') : jq.id.find('[data-carousel="true"]'));
	if (sections.length === 0) {
		throw tiaacref.error('', 'No carousel sections found (data-carousel="true") for DOM element in arg.id in carousel');
	}
	
	if (jq.adviceContainer.length !== 0 && jq.adviceContent.length !== 0) {
		adviceSections = jq.adviceContent.find('[data-index]');
		jq.adviceContainer.removeClass('hidden').empty().append(jq.adviceContent);
	} else if (jq.adviceContainer.length !== 0){
		adviceSections = null;
		jq.adviceContainer.addClass('hidden');
	}
	jq.id.css({position: 'relative'});
	currentSlide = 0;
	dataIndex = [];

	this.arg = arg;
	this.jq = jq;
	this.sections = sections;
	this.adviceSections = adviceSections;
	this.currentSlide = currentSlide;
	this.dataIndex = dataIndex;
	this.doChange = true;
	this.animationInProgress = false;
	
	self = this;

	if (adviceSections !== null) {
		adviceSections.each(function(index) {
			"use strict";
			var element = $(this);
			element.addClass('hidden').removeClass('active').attr({'aria-hidden': true});
			if (index === self.currentSlide) {
				element.addClass('active').removeClass('hidden').attr({'aria-hidden': false});
			}
		});
	}
	
	
	jq.id.mouseenter({self: this}, function(e) {
		e.data.self.doChange = false;
	});
	jq.id.mouseleave({self: this}, function(e) {
		e.data.self.doChange = true;
	});
	// do the styles
	if (arg.style === 'marketing') { 
		sections.each(function(index) {
			"use strict";
			var element = $(this),
				width = element.outerWidth(true),
				height = element.outerHeight(true),
				bxWindow = self.jq.id.find('.bx-window'),
				windowWidth = bxWindow.width(),
				windowHeight = bxWindow.height();
			element.css({position: 'absolute', top: (windowHeight - height)/2, left: (windowWidth - width)/2})
//				.attr({id: 'tcIdCarousel' + index}).fadeOut(10);
				.attr({id: 'tcIdCarousel' + index}).fadeTo(10, 0);
			if (index === 0) {
//				element.show();
				element.fadeTo(10, 1);
			}
		});
		links=jq.id.find('.bx-pager').first().empty();
		for (i = 0, l = sections.length; i < l; i++) {
			addClass = (this.currentSlide === i ? 'pager-active' : '');
			links.append($('<a>').attr({role: 'button', href: 'javascript:void(0);', 'data-index': i}).text(i).addClass(addClass + ' pager-link')
				.click({self: this, index: i}, function(e) {
					var self = e.data.self,
						sections = self.sections,
						index = e.data.index,
						currentSlide = self.currentSlide;
					self.jq.id.find('div.bx-pager a')
						.removeClass('pager-active')
						.filter('[data-index="'+index+'"]')
						.addClass('pager-active');
//					$(sections[currentSlide]).fadeOut('slow');
//					$(sections[index]).fadeIn('slow');
					$(sections[currentSlide]).fadeTo(1000, 0);
					$(sections[index]).fadeTo(1000, 1);
					self.currentSlide = index;
					tcoipc.fire(self.getCurrentSlideEventName(), currentSlide);
					return false;
				}));
		}
		tiaacref.carouselData[arg.id] = this;
		$(this.sections[this.currentSlide]).fadeIn('slow');
		if (sections.length <= 1) {
			links.addClass('hidden');
		} else {
			if (arg.autoMove) {
				setTimeout('tiaacref.carouselAdvance("' + arg.id + '")', arg.delay);
			}
		}
	} else if (arg.style === 'advice') {
		var totalLIWidth = 0,
			containerWidth = 0,
			num = arg.adviceCarouselCount,
			liHeight = 0;
		sections.each(function(index) {
			"use strict";
			var element = $(this);
			dataIndex.push(element.attr('data-index'));
			element.css({float: 'left', overflow: 'hidden'})
				.attr({role: 'button', tabindex: 0});
			totalLIWidth += element.outerWidth(true);
			liHeight = Math.max(element.outerHeight(true), liHeight);
			if (num > 0) {
				containerWidth += element.outerWidth(true);
			}
			num --;
			element.click({self: self, element: element}, function(e) {
				"use strict";
				e.data.self.showPage(parseInt(e.data.element.attr('data-index')));
			});
		});
		jq.id.find('.bx-wrapper').css({width: containerWidth});
		jq.id.find('ul').css({overflow: 'hidden', height: liHeight, position: 'relative', left: 0, width: totalLIWidth});
		links = {
			next: $('<a>')
				.attr({tabindex: 0, title: 'Next', 'aria-label': 'Next', 'aria-required': false, role: 'button', href: 'javascript:void(0);'})
				.addClass('bx-next')
//				.text('next')
				.click({self: this}, function (e) {
					var self = e.data.self,
						ul = self.jq.id.find('ul'),
						ulLeft = ul.position().left,
						li = ul.find('li:nth-child(1)'),
						liWidth = li.outerWidth(true);
					if (self.animationInProgress) {
						return false;
					}
					self.animationInProgress = true;
					ul.animate(
							{left: ulLeft - liWidth},
							{duration: self.arg.animationDuration, easing: 'linear', complete: function() {
								ul.append(li);
								ul.css({left: 0});
								self.animationInProgress = false;
							}}
					);
					return false;
				})
				.prepend('<span class="icon"></span>'),
			prev: $('<a>')
				.attr({tabindex: 0, 'aria-label': 'Prev', 'aria-required': false, title: 'Prev', role: 'button', href: 'javascript:void(0);'})
				.addClass('bx-prev')
//				.text('prev')
				.click({self: this}, function (e) {
					var self = e.data.self,
						ul = self.jq.id.find('ul'),
						ulLeft = ul.position().left,
						liWidth = ul.find('li').first().outerWidth(true),
						l = self.sections.length;
					if (self.animationInProgress) {
						return false;
					}
					self.animationInProgress = true;
					ul.css({left: liWidth * -1});
					ul.prepend(ul.find('li:nth-child(' + l + ')'));
					ul.animate(
							{left: 0},
							{duration: self.arg.animationDuration, easing: 'linear', complete: function() {
								self.animationInProgress = false;
							}}
					);
					return false;
				})
				.prepend('<span class="icon"></span>')
		};
		jq.id.find('.bx-window').after(links.prev);
		jq.id.find('.bx-window').after(links.next);
	} else if (arg.style === 'portfolio') {
		currentSlide = 0;
		sections.each(function(i) {
			var section = $(this);
			dataIndex.push(section.attr('data-index'));
			section.addClass('hidden').attr({'aria-hidden': true});
		});
		sections.filter('[data-index="' + dataIndex[currentSlide] + '"]').removeClass('hidden').attr({'aria-hidden': false});
		links = {
			prev: $('<button>')
				.attr({tabindex: 0, 'aria-label': 'Prev', 'aria-required': false, title: 'Prev', role: 'button', href: 'javascript:void(0);'})
				.addClass('bx-prev')
//				.text('prev')
				.click({self: this}, function (e) {
					
					var self = e.data.self,
						currentSlide = self.currentSlide,
						sections = self.sections,
						dataIndex = self.dataIndex;					
					currentSlide --;
					if (currentSlide < 1) {
						currentSlide = sections.length;
					}
					self.showPage(currentSlide);
					tcoipc.fire(self.getPreviousSlideEventName(), currentSlide);
					return false;
				}),
			next: $('<button>')
				.attr({tabindex: 0, title: 'Next', 'aria-label': 'Next', 'aria-required': false, role: 'button', href: 'javascript:void(0);'})
				.addClass('bx-next')
//				.text('next')
				.click({self: this}, function (e) {
					var self = e.data.self,
						currentSlide = self.currentSlide,
						sections = self.sections,
						dataIndex = self.dataIndex;
					currentSlide	++;
					if (currentSlide > sections.length) {
						currentSlide = 1;
					}
					self.showPage(currentSlide);
					tcoipc.fire(self.getNextSlideEventName(), currentSlide);
					return false;
				})
			
		};
		jq.id.find('.bx-window').after(links.prev);
		jq.id.find('.bx-window').after(links.next);
	} else if (arg.style === 'simple') {
		this.calculateHeight();
		nextButton = jq.id.find('button.bx-next');
		previousButton = jq.id.find('button.bx-prev');
		nextButton.on('click', {self: this}, function (event) {
			var self = event.data.self,
				currentSlide = self.currentSlide,
				sections = self.sections;
			event.stopPropagation();
			event.preventDefault();
			currentSlide ++;
			if (currentSlide > sections.length) {
				currentSlide = 1;
			}
			self.showPage(currentSlide);
			tcoipc.fire(self.getNextSlideEventName(), currentSlide);
		});
		previousButton.on('click', {self: this}, function (event) {
			var self = event.data.self,
				currentSlide = self.currentSlide,
				sections = self.sections;
			event.stopPropagation();
			event.preventDefault();
			currentSlide --;
			if (currentSlide < 1) {
				currentSlide = sections.length;
			}
			self.showPage(currentSlide);
			tcoipc.fire(self.getNextSlideEventName(), currentSlide);
		});
		currentSlide = (arg.startPage < 1 || arg.startPage >= sections.length ? 1 : arg.startPage);
		sections.each(function (index) {
			$(this).attr({'data-index': index + 1});
		});
		self.showPage(currentSlide);
	}
	return;
};

tiaacref.carousel.prototype.calculateHeight = function () {
	"use strict";
	var ul,
		height;
	if (this.arg.style === 'simple') {
		ul = this.jq.id.find('ul').first();
		ul.addClass('nlist rel').css({visibility: 'visible', display: 'block'});
		// find height (TODO: fix this - there has to be a better way of getting the height when contents can be floated.)
		if (tiaacref.isIE().ie) {
			this.sections.each(function (index) {
				$(this).css({zoom: 1});
			});
		}
		height = -1;
		ul.children().each(function (uIndex) {
			height = Math.max(height, $(this).outerHeight(true));
			$(this).children().each(function (dIndex) {
				height = Math.max(height, $(this).outerHeight(true));
			});
		});
		ul.css({visibility: '', display: ''}).parent().height(height);
	}
};

tiaacref.carouselAdvance = function (id) {
	"use strict";
	var defaults = tiaacref.defaults.carousel,
		self,
		oldSlide;
	if (typeof tiaacref.carouselData[id] === 'undefined') {
		return;
	}
	self = tiaacref.carouselData[id];
	if (self.sections.length === 1) {
		return;
	}
	if (self.doChange && tiaacref.WindowActive) {
		oldSlide = self.currentSlide;
		self.currentSlide++;
		if (self.currentSlide >= self.sections.length) {
			self.currentSlide = 0;
		}
		self.recenter();
//		$(self.sections[oldSlide]).fadeOut('slow');
//		$(self.sections[self.currentSlide]).fadeIn('slow');
		$(self.sections[oldSlide]).fadeTo(1000, 0);
		$(self.sections[self.currentSlide]).fadeTo(1000, 1);
		self.jq.id.find('div.bx-pager a')
			.removeClass('pager-active')
			.filter('[data-index="'+self.currentSlide+'"]')
			.addClass('pager-active');
		tcoipc.fire(self.getCurrentSlideEventName(), self.currentSlide);
	}
	if (self.arg.autoMove) {
		setTimeout('tiaacref.carouselAdvance("' + id + '")', self.arg.delay);
	}
};
tiaacref.carousel.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};
tiaacref.carousel.prototype.getNextSlideEventName = function () {
	"use strict";
	return tiaacref.defaults.carousel.nextSlideEventName + this.getId();
};
tiaacref.carousel.prototype.getPreviousSlideEventName = function () {
	"use strict";
	return tiaacref.defaults.carousel.previousSlideEventName + this.getId();
};
tiaacref.carousel.prototype.getCurrentSlideEventName = function () {
	"use strict";
	return tiaacref.defaults.carousel.currentSlideEventName + this.getId();
};
tiaacref.carousel.prototype.showPage = function (page) {
	"use strict";
	var defaults = tiaacref.defaults.carousel,
		sections = this.sections;
	if (typeof page !== 'number') {
		throw tiaacref.error('badId', page, 'page is not numeric in carousel.showPage');
	}
	this.currentSlide = page;
	if (this.arg.style === 'advice') {
		this.sections.removeClass('active').filter('[data-index="' + page + '"]').addClass('active');
	} else {
		this.sections.addClass('hidden').attr({'aria-hidden': true})
			.filter('[data-index="' + page + '"]').removeClass('hidden').attr({'aria-hidden': false});
	}
	tcoipc.fire(this.getCurrentSlideEventName(), page);
	if (this.adviceSections !== null) {
		this.adviceSections.addClass('hidden').attr({'aria-hidden': true})
			.filter('[data-index="' + page + '"]').removeClass('hidden').attr({'aria-hidden': false});
	}
	return false;
	
};
tiaacref.carousel.prototype.recenter = function () {
	"use strict";
	var jq = this.jq,
		arg = this.arg,
		sections = this.sections;
	if (arg.style !== 'marketing') {
		throw tiaacref.error('', 'Not able to do a recenter() method on a non-marketing carousel.');
	}
sections.each(function(index) {
		"use strict";
		var element = $(this),
			width = element.outerWidth(true),
			height = element.outerHeight(true),
			bxWindow = jq.id.find('.bx-window'),
			windowWidth = bxWindow.width(),
			windowHeight = bxWindow.height();
		width = Math.min(width, windowWidth);
		height = Math.min(height, windowHeight);
		element.css({position: 'absolute', top: (windowHeight - height)/2, left: (windowWidth - width)/2});
		
	});
};
tiaacref.carousel.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('carousel.' + eventName) + this.arg.id;
};

































/* ================================================ PAGINATE ==================================== */
tiaacref.paginate = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.paginate,
		jq,
		self,
		inc,
		nid,
		backLink,
		nextLink,
		i,
		l,
		perPageDropDownValue,
		pages,
		pageLink,
		duplicateSortName = {},
		thSortName,
		tbody,
		tr,
		tag,
		td,
		th,
		trCollection;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			table: defaults.table,
			startPage: defaults.startPage,
			perPage: defaults.perPage,
			expandCollapse: defaults.expandCollapse,
			autoCount: defaults.autoCount,
			multiSort: defaults.multiSort,
			showPerPageDropDown: defaults.showPerPageDropDown,
			dataSource: defaults.dataSource,
			style: defaults.style,
			pageListingLimit: defaults.pageListingLimit,
			showViewingText: defaults.showViewingText,
			perPageDropDownValues: defaults.perPageDropDownValues
		};
	} else {
		if (typeof arg.table !== 'string') {
			arg.table = defaults.table;
		}
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.perPage !== 'number') {
			arg.perPage = defaults.perPage;
		}
		if (typeof arg.startPage !== 'number') {
			arg.startPage = defaults.startPage;
		}
		if (typeof arg.expandCollapse !== 'boolean') {
			arg.expandCollapse = defaults.expandCollapse;
		}
		if (typeof arg.autoCount !== 'boolean') {
			arg.autoCount = defaults.autoCount;
		}
		if (typeof arg.multiSort !== 'boolean') {
			arg.multiSort = defaults.multiSort;
		}
		if (typeof arg.showPerPageDropDown !== 'boolean') {
			arg.showPerPageDropDown = defaults.showPerPageDropDown;
		}
		if (typeof arg.dataSource !== 'string' && typeof arg.dataSource !== 'object') {
			arg.dataSource = defaults.dataSource;
		}
		if (arg.pageListingLimit == null) {
			arg.pageListingLimit = defaults.pageListingLimit;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof defaults.validStyle[arg.style] === 'undefined') {
			arg.style = defaults.style;
		}
		if (!arg.perPageDropDownValues instanceof Array) {
			arg.perPageDropDownValues = defaults.perPageDropDownValues;
		}
		if (typeof arg.showViewingText !== 'boolean') {
			arg.showViewingText = defaults.showViewingText;
		}
	}
	if (arg.perPage <= 0) {
		throw tiaacref.error('', 'arg.perPage is equal to or less than 0 for paginate.');
	}
	jq = ({id: $(arg.id), table: $(arg.table)});
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'paginate');
	}
	if (jq.table.length === 0) {
		throw tiaacref.error('badId', arg.table, 'paginate');
	}
	this.arg = arg;
	this.jq = jq;
	this.numRecords = 0;
	this.tableTemplate = null;
	this.usingDataSource = null;
	this.tagList = [];
	// save table to defaults
	if (typeof defaults.connectWith[arg.table] === 'undefined') {
		defaults.connectWith[arg.table] = [];
	}
	self = this;
	defaults.connectWith[arg.table].push(function(page) {
		self.changePageRender(page);
	});
	if (arg.autoCount === true) {
		tcoipc.on(tiaacref.eventName('paginate.drawTable') + arg.table, function(data) {
			self.draw();
		});
		tcoipc.on(tiaacref.eventName('paginate.drawTable') + arg.id, function(data) {
			self.draw();
		});
		tcoipc.on(tiaacref.eventName('paginate.drawPagination') + arg.id, function(data) {
			self.drawPagination();
		});
	}
	tcoipc.on(tiaacref.eventName('paginate.drawPagination') + arg.table, function(data) {
		self.drawPagination();
	});
	tcoipc.on(tiaacref.eventName('paginate.recalcDataSource') + arg.table, function(data) {
		self.recalcDataSource();
	});
	tcoipc.on(tiaacref.eventName('paginate.setDataSource') + arg.table, function(data) {
		self.setDataSource(data);
	});
	// set up num records listener. Even if autoCount is true, this is setup in case we need to change 
	// the number of records later.
	self = this;
	tcoipc.on(this.getNumRecordsEventName(), function (num) {
		self.numRecords = num;
	});
	tcoipc.on(tiaacref.eventName('paginate.setTableNumRecords') + arg.table, function (num) {
		self.numRecords = num;
	});
	//
	// check for datasource
	//
	if (arg.dataSource instanceof Array) {
		this.usingDataSource = 'js';
	} else if (arg.dataSource !== '') {
		this.usingDataSource = 'url';
	} else {
		this.usingDataSource = '';
	}
	if (this.usingDataSource === 'js') {
		jq.table.children('thead').first().children('tr').first().children('th').each(function(index) {
			th = $(this);
			thSortName = th.attr('data-sortname');
			self.tagList.push({
				name: thSortName,
				moreHide: th.attr('data-sortmorehide'),
				format: th.attr('data-sortformat')
			});
			if (thSortName != undefined) {
				if (typeof duplicateSortName[thSortName] !== 'undefined') {
					console.log("WARNING: Duplicate data-sortnames found (name = " + thSortName + ", table = " + self.arg.id + ")");
				}
				duplicateSortName[thSortName] = true;
			}
		});
		this.tableTemplate = jq.table.children('tbody').first().children('tr').first().clone(true);
		this.numRecords = arg.dataSource.length;
	} else {
		//
		// get records
		//
		if (arg.autoCount === true) {
			if (jq.table.get(0).tagName.toLowerCase() === 'table') {
				trCollection = jq.table.children('tbody').first().children('tr');
				if (arg.multiSort === true) {
					trCollection = trCollection.filter('[data-sortmultisort="true"]');
				}
				trCollection.each(function(i) {
					if ($(this).attr('data-sortfiltered') == undefined) {
						$(this).attr({'data-sortfiltered': false});
					}
				});
				trCollection = trCollection.filter('[data-sortfiltered="false"]');
				this.numRecords = trCollection.length;
			}
		} else {
			tcoipc.fire(
					this.getNumRecordsEventNameForTable(),
					{expandcollapse: arg.expandCollapse, event: this.getNumRecordsEventName()}
				);
		}
	}
	
	inc = Math.floor(this.numRecords / arg.perPage);
	if (this.numRecords % arg.perPage !== 0) {
		inc += 1;
	}
	this.maxPages = inc;
	this.arg.startPage = Math.min(this.maxPages, this.arg.startPage);
	this.arg.startPage = Math.max(1, this.arg.startPage);
	this.currentPage = this.arg.startPage;
	if (this.usingDataSource === 'js') {
		this.draw();
	}
	this.drawPagination();
	//
	// set initial page and send it to the table
	//
	tcoipc.fire(
		this.getPaginateEventName(),
		{pagenumber: this.arg.startPage, perpage: this.arg.perPage, expandcollapse: this.arg.expandCollapse}
	);
	if (arg.autoCount === true) {
		this.draw();
	}
	return;
};


tiaacref.paginate.prototype.recalcDataSource = function () {
	"use strict";
	var defaults = tiaacref.defaults.paginate,
		jq = this.jq,
		arg = this.arg,
		self = this,
		trObject;
	if (this.usingDataSource === 'js') {
		this.arg.dataSource = [];
		jq.table.children('tbody').first().children('tr').each(function(index) {
			trObject = {};
			$(this).children('td').each(function(index) {
				trObject[self.tagList[index].name] = $(this).attr('data-sortvalue');
			});
			trObject['data-sortfiltered'] = $(this).attr('data-sortfiltered');
			self.arg.dataSource.push(trObject);
		});
	}
	return;
};

tiaacref.paginate.prototype.setDataSource = function(data) {
	"use strict";
	if (this.usingDataSource === 'js') {
		this.arg.dataSource = data;
		this.draw();
		this.drawPagination();
	}
};


tiaacref.paginate.prototype.draw = function () {
	"use strict";
	var defaults = tiaacref.defaults.paginate,
		jq = this.jq,
		arg = this.arg,
		currentPage = this.currentPage,
		perPage = arg.perPage,
		autoCount = arg.autoCount,
		multiSort = arg.multiSort,
		tr,
		td,
		moreHideAttr,
		moreHideValue,
		moreHideList,
		startTR,
		endTR,
		indexTR,
		innerTR,
		detailTR,
		trMultiSort,
		showMultiSort = false,
		tbody,
		l,
		i,
		j,
		k,
		tag,
		text,
		sortValueText,
		htmlValueText,
		filtered,
		self = this;
	startTR = (currentPage - 1) * perPage;
	if (this.usingDataSource === 'js') {
		endTR = (startTR + perPage >= arg.dataSource.length ? arg.dataSource.length : startTR + perPage);
		tbody = jq.table.children('tbody').first();
		tbody.empty();
		l = arg.dataSource.length;
		indexTR = 0;
		for (i = 0; i < l; i++) {
			moreHideList = [];
			tag = arg.dataSource[i];
			if (typeof tag['data-sortfiltered'] === 'undefined') {
				tag['data-sortfiltered'] = "false";
			}
			filtered = tag['data-sortfiltered'];
			tr = this.tableTemplate.clone(true);
			tr.children('td').each(function(index) {
				td = $(this);
				moreHideAttr = self.tagList[index].moreHide; 
				text = self.tagList[index].name;
				sortValueText = (tag[text] === null ? '' : tag[text]);
				htmlValueText = (self.tagList[index].format == undefined ? sortValueText : tiaacref.format(sortValueText, self.tagList[index].format));
				td.html(htmlValueText).attr({'data-sortvalue': sortValueText});
				if (moreHideAttr != undefined) {
					moreHideValue = parseInt(moreHideAttr);
					if (isNaN(moreHideValue)) {
						moreHideValue = tiaacref.defaults.tableSort.moreHideSize;
					}
					td.attr({id: self.arg.table.substring(1) + 'mh' + index + 'r' + i});
					moreHideList.push(self.arg.table + 'mh' + index + 'r' + i);
				}
			});
			if (filtered.toLowerCase() === "false") {
				if (indexTR < startTR || indexTR >= endTR) {
					tr.addClass('hidden');
				} else {
					tr.removeClass('hidden');
				}
				indexTR ++;
			} else {
				tr.addClass('hidden');
			}
			tr.attr({'data-sortfiltered': filtered});
			tbody.append(tr);
			k = moreHideList.length;
			for (j = 0; j < k; j++) {
				new tiaacref.expandCollapse({
					header: moreHideList[j],
					content: moreHideList[j],
					style: 'moreHide',
					moreHideSize: moreHideValue
				});
			}
		}
		tcoipc.fire(tiaacref.eventName('tableSort.reloadSortData') + this.arg.table, {});
	} else {
		if (arg.autoCount === true) {
			if (jq.table.get(0).tagName.toLowerCase() === 'table') {
				startTR = (currentPage - 1) * perPage;
				endTR = startTR + perPage;
				indexTR = 0;
				showMultiSort = false;
				jq.table.children('tbody').first().children('tr').each(function(index) {
					tr = $(this);
					trMultiSort = (tr.attr('data-sortmultisort') == undefined ? "false" : tr.attr('data-sortmultisort'));
					if (self.arg.multiSort === true && trMultiSort.toLowerCase() !== "true") {
						if (showMultiSort === true) {
							tr.removeClass('hidden');
						} else {
							tr.addClass('hidden');
						}
					} else {
						if (tr.attr('data-sortfiltered').toLowerCase() === "false") {
							if (indexTR >= startTR && indexTR < endTR) {
								tr.removeClass('hidden');
								showMultiSort = true;
							} else {
								tr.addClass('hidden');
								showMultiSort = false;
							}
							indexTR ++;
						} else {
							tr.addClass('hidden');
							showMultiSort = false;
						}
					}
				});
				
			}
		}
	}
	return;
};



tiaacref.paginate.prototype.drawPagination = function () {
	"use strict";
	var defaults = tiaacref.defaults.paginate,
		arg = this.arg,
		numRecords = this.numRecords,
		perPage = this.arg.perPage,
		pageListingLimit = this.arg.pageListingLimit,
		pageListingPadding = Math.floor(pageListingLimit / 2),
		pageListingLowerLimit,
		pageListingUpperLimit,
		pageListingLowerLimitSet = false,
		pageListingUpperLimitSet = false,
		paginateId = this.jq.id,
		backLink = '',
		nextLink = '',
		pages = $('<div>').addClass('pages'),
		pageLink,
		i,
		jq = this.jq,
		l,
		option,
		perPageDropDownValue,
		self = this,
		pageDropDownMax = Math.max.apply(Math, self.arg.perPageDropDownValues),
		pageDropDownMin = Math.min.apply(Math, self.arg.perPageDropDownValues),
		inc = Math.floor(numRecords / perPage);
	if (numRecords % perPage !== 0) {
		inc += 1;
	}
	this.maxPages = inc;
	if (this.currentPage > this.maxPages) {
		this.currentPage = this.maxPages;
	} else if (this.currentPage == 0 && this.maxPages !== 0) {
		this.currentPage = 1;
	}
	pageListingLowerLimit = Math.max(this.currentPage - pageListingPadding, 1);
	pageListingUpperLimit = Math.max(this.currentPage + pageListingPadding, pageListingLimit);
	pageListingLowerLimit = Math.min(pageListingLowerLimit, inc - pageListingLimit +1);
	pageListingUpperLimit = Math.min(pageListingUpperLimit, inc);
	// we have to subtract 1 from the number of pages if the current page is the next to last or next to first page
	if (this.currentPage - pageListingPadding === 2) {
		pageListingUpperLimit --;
	}
	if (this.currentPage + pageListingPadding === inc - 1) {
		pageListingLowerLimit ++;
	}
	if (arg.style === 'new') {
		i = arg.id.substring(1);
		tiaacref.showElement(jq.id.find('ul.pagingList'));
		jq.pagingLabel = $('#paginglabel' + i);
		jq.pageSelect = $('#pageselect' + i);
		jq.gotoPageLabel = $('#gotoPagelabel' + i);
		jq.gotoPageInput = $('#gotoPage' + i);
		jq.ofPages = $('#ofPages' + i);
		jq.pageLabel = $('#pageLabel' + i);
		jq.prev = jq.id.find('a[rel="prev"]');
		jq.next = jq.id.find('a[rel="next"]');
		jq.goButton = jq.id.find('input[type="button"]');
		jq.invalidPage = jq.id.find('#invalidPage' + i);
		jq.viewingText = {
			text: jq.id.find('#viewingText' + i),
			low: jq.id.find('#viewingTextLow' + i),
			high: jq.id.find('#viewingTextHigh' + i),
			total: jq.id.find('#viewingTextTotal' + i)
		};
		// viewing text
		jq.viewingText.total.text(this.numRecords);
		jq.viewingText.low.text(Math.min(   (this.currentPage - 1) * perPage + 1, this.numRecords));
		jq.viewingText.high.text(Math.min(  (this.currentPage - 1) * perPage + perPage, this.numRecords));
		if (arg.showViewingText) {
			tiaacref.showElement(jq.viewingText.text);
		} else {
			tiaacref.hideElement(jq.viewingText.text);
		}
		if (inc > 1) {
			jq.ofPages.text(inc);
			if (arg.selectPage === true) {
				tiaacref.showElement(jq.gotoPageLabel);
				tiaacref.showElement(jq.gotoPageInput);
				tiaacref.showElement(jq.goButton);
				tiaacref.hideElement(jq.pageLabel);
				jq.gotoPageInput.val(this.currentPage);
				jq.goButton.off('click').on('click', function(event) {
					var page = parseInt(jq.gotoPageInput.val(), 10)
					if (jq.gotoPageInput.val().match(/[^0-9]/gi) != null || page < 1 || page > self.maxPages || isNaN(page)) {
						self.jq.gotoPageInput.addClass('alertInput');
						tiaacref.showElement(jq.id.find('.alertText'));
						tiaacref.showElement(self.jq.invalidPage);
					} else {
						self.jq.gotoPageInput.removeClass('alertInput');
						tiaacref.hideElement(jq.id.find('.alertText'));
						tiaacref.hideElement(self.jq.invalidPage);
						event.preventDefault();
						event.stopPropagation();
						self.changePage(page);
					}
				});
			} else {
				tiaacref.hideElement(jq.gotoPageLabel);
				tiaacref.hideElement(jq.gotoPageInput);
				tiaacref.hideElement(jq.goButton);
				tiaacref.showElement(jq.pageLabel);
				jq.pageLabel.find('span').text(this.currentPage);
			}
		} else {
			tiaacref.hideElement(jq.id.find('ul.pagingList'));
		}
		if (arg.showPerPageDropDown === true && this.numRecords >= pageDropDownMin) {
			jq.pageSelect.empty();
			l = arg.perPageDropDownValues.length;
			for (i = 0; i < l; i++) {
				perPageDropDownValue = arg.perPageDropDownValues[i];
				option = $('<option>').attr({value: perPageDropDownValue}).append(perPageDropDownValue);
				if (perPageDropDownValue === perPage) {
					option.attr({selected: 'selected'});
				}
				jq.pageSelect.append(option);
			}
			jq.pageSelect.off('change.pageinate.cl').on('change.paginate.cl', function(event) {
				var element = $(this);
				tcoipc.fire(self.event('recordsPerPage'), element.val());
				self.setPerPage(parseInt(element.val()));
			});
			tiaacref.showElement(jq.pageSelect.parent().children('label,select'));
		} else {
			tiaacref.hideElement(jq.pageSelect.parent().children('label,select'));
		}
		if (this.currentPage === 1) {
			tiaacref.hideElement(jq.prev.parent());
		} else {
			tiaacref.showElement(jq.prev.parent());
			jq.prev.off('click').on('click', function(event) {
				self.jq.gotoPageInput.removeClass('alertInput');
				tiaacref.hideElement(jq.id.find('.alertText'));
				event.preventDefault();
				event.stopPropagation();
				tcoipc.fire(self.event('previousLink'), {});
				self.changePage(self.currentPage - 1);
			});
		}
		if (this.currentPage === this.maxPages) {
			tiaacref.hideElement(jq.next.parent());
			jq.next.parent().prev().addClass('last');
		} else {
			tiaacref.showElement(jq.next.parent());
			jq.next.parent().prev().removeClass('last');
			jq.next.off('click').on('click', function(event) {
				self.jq.gotoPageInput.removeClass('alertInput');
				tiaacref.hideElement(jq.id.find('.alertText'));
				event.preventDefault();
				event.stopPropagation();
				tcoipc.fire(self.event('nextLink'), {});
				self.changePage(self.currentPage + 1);
			});
		}
	} else if (arg.style === 'normal') {
		paginateId.empty();
		if (inc > 1) {
			backLink = $('<a>')
				.attr({href: 'javascript:void(0);'})
				.addClass('backLink')
				.html('<span class="icon"></span>Previous')
				.click({self: this}, function (e) {
					tcoipc.fire(e.data.self.event('previousLink'), {});
					e.data.self.changePage(e.data.self.currentPage - 1);
					return false;
				});
			nextLink = $('<a>')
				.attr({href: 'javascript:void(0);'})
				.addClass('nextLink')
				.html('<span class="icon"></span>Next')
				.click({self: this}, function (e) {
					tcoipc.fire(e.data.self.event('nextLink'), {});
					e.data.self.changePage(e.data.self.currentPage + 1);
					return false;
				});
			for (i = 1; i <= inc; i++) {
				pageLink = '';
				if (i === 1 || i === inc || (i >= pageListingLowerLimit && i <= pageListingUpperLimit) ) {
					pageLink = $('<a>')
						.attr({href: 'javascript:void(0);'})
						.text(i)
						.attr({'data-page': i});
				} else if (i < pageListingLowerLimit && !pageListingLowerLimitSet) {
					pageListingLowerLimitSet = true;
					pageLink = $('<span>').text('...');
				} else if (i > pageListingUpperLimit && !pageListingUpperLimitSet) {
					pageListingUpperLimitSet = true;
					pageLink = $('<span>').text('...');
				}
				if (this.currentPage === i) {
					pageLink.addClass('selected');
				}
				if (this.currentPage === 1) {
					backLink.css({visibility: 'hidden'});
				}
				if (this.currentPage === this.maxPages) {
					nextLink.css({visibility: 'hidden'});
				}
				pages.append(pageLink);
			}
			pages.find('a').click({self: this}, function (e) {
				tcoipc.fire(e.data.self.event('selectPage'), parseInt($(this).attr('data-page'), 10));
				e.data.self.changePage(parseInt($(this).attr('data-page'), 10));
				return false;
				});
		}
		if (arg.showPerPageDropDown === true) {
			pageLink = $('<select>').addClass('mllg mtnxxs').attr({id: 'select' + arg.id});
			l = arg.perPageDropDownValues.length;
			for (i = 0; i < l; i++) {
				perPageDropDownValue = arg.perPageDropDownValues[i];
				option = $('<option>').attr({value: perPageDropDownValue}).text(perPageDropDownValue);
				if (perPageDropDownValue === perPage) {
					option.attr({selected: 'selected'});
				}
				pageLink.append(option);
			}
			pageLink.change({self: this}, function(e) {
				"use strict";
				var self = e.data.self;
				tcoipc.fire(self.event('recordsPerPage'), $(this).val());
				self.setPerPage(parseInt($(this).val()));
			});
			pages.append(pageLink);
		}
		paginateId.append(backLink).append(pages).append(nextLink);
	} else if (arg.style === 'ud') {
		pages = $('<ul>');
	}
	return;
};
tiaacref.paginate.prototype.changePageRender = function (page) {
	"use strict";
	// first, see if we need to rebuild the pagination
	this.currentPage = page;
	this.drawPagination();
	return;
};
tiaacref.paginate.prototype.changePage = function (page) {
	"use strict";
	var defaults = tiaacref.defaults.paginate,
		l = defaults.connectWith[this.arg.table].length,
		i = 0;
	for (i = 0; i < l; i ++) {
		defaults.connectWith[this.arg.table][i](page);
	}
	tcoipc.fire(
		this.getPaginateEventName(),
		{pagenumber: page, perpage: this.arg.perPage, expandcollapse: this.arg.expandCollapse}
	);
	if (this.arg.autoCount === true || this.usingDataSource === 'js') {
		this.draw();
	}
	return;
};
tiaacref.paginate.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};
tiaacref.paginate.prototype.getTableId = function () {
	"use strict";
	return this.arg.table;
};
tiaacref.paginate.prototype.getNumRecordsEventName = function () {
	"use strict";
	return tiaacref.eventName('paginate.numRecords') + this.arg.id;
};
tiaacref.paginate.prototype.getNumRecordsEventNameForTable = function () {
	"use strict";
	return tiaacref.eventName('paginate.numRecords') + this.arg.table;
};
tiaacref.paginate.prototype.getPaginateEventName = function () {
	"use strict";
	return tiaacref.eventName('paginate.currentPage') + this.arg.table;
};
tiaacref.paginate.prototype.getCurrentPageEventName = function () {
	"use strict";
	return tiaacref.eventName('paginate.currentPage') + this.arg.table;
};
tiaacref.paginate.prototype.getPerPage = function () {
	"use strict";
	return this.arg.perPage;
};
tiaacref.paginate.prototype.getCurrentNumber = function() {
	"use strict";
	return this.currentPage;
};
tiaacref.paginate.prototype.setPerPage = function (perPage) {
	"use strict";
	var numRecords = this.numRecords,
		inc = Math.floor(numRecords / perPage);
	if (typeof perPage !== 'number') {
		throw tiaacref.Error('badId', perPage, 'paginate.setPerPage');
	};
	this.arg.perPage = perPage;
	tcoipc.fire(
		this.getPaginateEventName(),
		{pagenumber: this.currentPage, perpage: this.arg.perPage, expandcollapse: this.arg.expandCollapse}
	);
	// set the new page number (if required)
	if (numRecords % perPage !== 0) {
		inc += 1;
	}
	this.maxPages = inc;
	if (this.currentPage > this.maxPages) {
		this.currentPage = this.maxPages;
	}
	this.drawPagination();
	if (this.arg.autoCount === true) {
		this.draw();
	}
	return;
};

tiaacref.paginate.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('paginate.' + eventName) + this.arg.id;
};






/* ============================================ EXPAND COLLAPSE ===================================== */
tiaacref.expandCollapse = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.expandCollapse,
		jq,
		style,
		moreHideText,
		moreHideContent,
		moreHideSizeMod;
	if (typeof arg !== 'object') {
		arg = {
			header: defaults.header,
			content: defaults.content,
			style: defaults.style,
			disabled: defaults.disabled,
			startingCondition: defaults.startingCondition,
			onBeforeCollapse: defaults.onBeforeCollapse,
			onBeforeExpand: defaults.onBeforeExpand,
			onAfterCollapse: defaults.onAfterCollapse,
			onAfterExpand: defaults.onAfterExpand,
			moreHideSize: defaults.moreHideSize
		};
	} else {
		if (typeof arg.header !== 'string') {
			arg.header = defaults.header;
		}
		if (typeof arg.content !== 'string') {
			arg.content = defaults.content;
		}
		if (typeof arg.disabled !== 'boolean') {
			arg.disabled = defaults.disabled;
		}
		if (typeof arg.onBeforeCollapse !== 'function') {
			arg.onBeforeCollapse = defaults.onBeforeCollapse;
		}
		if (typeof arg.onBeforeExpand !== 'function') {
			arg.onBeforeExpand = defaults.onBeforeExpand;
		}
		if (typeof arg.onAfterCollapse !== 'function') {
			arg.onAfterCollapse = defaults.onAfterCollapse;
		}
		if (typeof arg.onAfterExpand !== 'function') {
			arg.onAfterExpand = defaults.onAfterExpand;
		}
		if (typeof arg.style != 'string') {
			arg.style = defaults.style;
		} else {
			if (typeof defaults.validStyle[arg.style] === 'undefined') {
				arg.style = defaults.style;
			}
		}
		if (typeof arg.startingCondition != 'string') {
			arg.startingCondition = defaults.startingCondition;
		} else {
			if (typeof defaults.validStartingCondition[arg.startingCondition] === 'undefined') {
				arg.startingCondition = defaults.startingCondition;
			}
		}
		if (typeof arg.moreHideSize !== 'number') {
			arg.moreHideSize = defaults.moreHideSize;
		}
		if (arg.moreHideSize === 0) {
			arg.moreHideSize = defaults.moreHideSize;
		}
	}
	style = defaults.validStyle[arg.style];
	if (arg.style === 'moreHide') {
		moreHideContent = $(arg.content);
		if (moreHideContent.length === 0) {
			throw tiaacref.error('badId', arg.content, 'expandCollapse');
		}
		moreHideText = moreHideContent.html();
		if (moreHideText.length <= arg.moreHideSize) {
			return; // do not create if not enough characters
		}
		moreHideSizeMod = arg.moreHideSize;
		while (moreHideText.substring(moreHideSizeMod - 1, moreHideSizeMod) !== ' ' && moreHideSizeMod > 0) {
			moreHideSizeMod--;
		}
/*		
		moreHideContent.html(moreHideText.substring(0, moreHideSizeMod) +
			' <button aria-hidden="false" role="button" aria-expanded="false" title="Select to expand the content below" class="moreText">More...</button> ' +
			'<span class="moreTextDetail hidden" aria-hidden="true">' +
			moreHideText.substring(moreHideSizeMod, moreHideText.length) +
			' <button href="javascript:void(0);" role="button" title="Select to collapse the content above" class="hideText">Hide</button> ' +
			'</span>'
			);
		arg.header += ' .moreText';
		arg.content += ' .moreTextDetail';
*/			
		moreHideContent.html(moreHideText.substring(0, moreHideSizeMod) +
			' <a class="addLink" href="javascript:void();" title="Show More"><span class="icon"></span><span class="text">Show More</span></a> ' +
			'<span class="moreTextDetail hidden" aria-hidden="true">' +
			moreHideText.substring(moreHideSizeMod, moreHideText.length) +
			' <a class="minusLink" href="javascript:void();" title="Show Less"><span class="icon"></span><span class="text">Show Less</span></a> ' +
			'</span>'
			);
		arg.header += ' .addLink';
		arg.content += ' .moreTextDetail';
	}
	jq = {
		header: $(arg.header),
		content: $(arg.content),
		buttons: {header: null, content: null}
	};
	if (jq.header.length === 0) {
		throw tiaacref.error('badId', arg.header, 'expandCollapse');
	}
	if (jq.content.length === 0) {
		throw tiaacref.error('badId', arg.content, 'expandCollapse');
	}
	// tr does hidden on content no matter what
	arg.doesHidden = false;
	arg.isTr = false;
	if (jq.content.get(0).tagName.toLowerCase()==='tr') {
		arg.doesHidden = true;
		arg.isTr = true;
	}
	this.creating = true;
	// pre-process the different styles
	switch (arg.style) {
	case 'alert':
		jq.buttons.header = jq.header.find(style.headerButton).first();
		jq.buttons.content = jq.content.find(style.contentButton).first();
		break;
	case 'panel':
		jq.buttons.headerButton = jq.header.find(style.headerButton).first();
		jq.buttons.header = jq.header;
		break;
	case 'simple':
		jq.buttons.header = jq.header.find(style.headerButton).first();
		break;
	case 'accountList':
		jq.buttons.header = jq.header.find(style.headerButton).first();
		break;
	case 'transactionList':
		jq.buttons.header = jq.header.find(style.headerButton).first();
		break;
	case 'showMore':
		break;
	case 'learnMore':
		break;
	case 'moreHide':
		jq.buttons.header = jq.header;
//		jq.buttons.content = jq.content.find('.hideText');
		jq.buttons.content = jq.content.find('.minusLink');
		break;
	default:
		break;
	};
	this.isExpanded = (arg.startingCondition === 'expanded' ? true : false);
	if (jq.buttons.header != null  && jq.buttons.header.length > 0) {
		jq.buttons.header.attr({role: 'button', 'aria-expanded': this.isExpanded, title: 'Select to expand or collapse content below.'});
	}
	if (jq.buttons.content != null && jq.buttons.content.length > 0) {
		jq.buttons.content.attr({role: 'button', 'aria-expanded': !this.isExpanded, title: 'Select to collapse this content.'});
	}
	if (arg.style !== 'showMore') {
		jq.content.attr({'aria-hidden': this.isExpanded});
	} else {
		jq.content.children('li').attr({'aria-hidden': this.isExpanded}).slice(0, style.collapsedSize).attr({'aria-hidden': false});
	}
	this.jq = jq;
	this.arg = arg;
	
	this.isExpanded = !this.isExpanded;
	this.toggle();
	// since setDisabled also sets the clicks, simply doing that routine will give us what we need.
	this.setDisabled(arg.disabled);
	this.creating = false;
};
tiaacref.expandCollapse.prototype.setDisabled = function (setting) {
	"use strict";
	var arg = this.arg,
		jq = this.jq,
		defaults = tiaacref.defaults.expandCollapse,
		style = defaults.validStyle[arg.style];
	if (typeof setting !== 'boolean') {
		arg.disabled = defaults.disabled;
	} else {
		arg.disabled = setting;
	}
	switch (arg.style) {
	case 'panel':
		if (arg.disabled === true) {
			jq.header.addClass(style.disabledClass);
			jq.header.unbind('click');
		} else {
			jq.header.removeClass(style.disabledClass);
			jq.buttons.header.click({self: this}, function (e) {
				"use strict";
				e.preventDefault();
				e.data.self.toggle();
				return false;
			});
		}
		break;
	case 'simple':
	case 'accountList':
	case 'transactionList':
		if (arg.disabled === true) {
			jq.header.addClass(style.disabledClass);
			jq.buttons.header.unbind('click');
		} else {
			jq.header.removeClass(style.disabledClass);
			jq.buttons.header.click({self: this}, function (e) {
				e.data.self.toggle();
				return false;
			});
		}
		break;
	case 'alert':
		if (arg.disabled === true) {
			jq.header.addClass(style.disabledClass);
			jq.content.addClass(style.disabledClass);
			jq.buttons.header.unbind('click');
			jq.buttons.content.unbind('click');
		} else {
			jq.header.removeClass(style.disabledClass);
			jq.content.removeClass(style.disabledClass);
			jq.buttons.header.click({self: this}, function (e) {
				e.data.self.toggle();
				return false;
			});
			jq.buttons.content.click({self: this}, function (e) {
				e.data.self.toggle();
				return false;
			});
		}
		break;
	case 'showMore':
	case 'learnMore':
		if (arg.disabled === true) {
		} else {
			jq.header.click({self: this}, function (e) {
				e.data.self.toggle();
				return false;
			});
		}
		break;
	case 'moreHide':
		if (arg.disabled === true) {
		} else {
			jq.buttons.header.on('click', {self: this}, function (e) {
				e.preventDefault();
				e.data.self.toggle();
			});
			jq.buttons.content.on('click', {self: this}, function (e) {
				e.preventDefault();
				e.data.self.toggle();
			});
		}
		break;
	default:
		if (arg.disabled === true) {
			jq.header.addClass(style.disabledClass);
			jq.header.unbind('click');
		} else {
			jq.header.click({self: this}, function (e) {
				e.data.self.toggle();
				return false;
			});
		}
		break;
	}
};
tiaacref.expandCollapse.prototype.toggle = function (fireUserEvents) {
	"use strict";
	var jq = this.jq;
	if (typeof fireUserEvents !== 'boolean') {
		fireUserEvents = true;
	}
	this.isExpanded = !this.isExpanded;
	if (this.isExpanded) {
		this.expand(fireUserEvents);
	} else {
		this.collapse(fireUserEvents);
	}
};
tiaacref.expandCollapse.prototype.expand = function (fireUserEvents) {
	"use strict";
	var jq = this.jq,
		arg = this.arg,
		style = tiaacref.defaults.expandCollapse.validStyle[arg.style];
	if (typeof fireUserEvents !== 'boolean') {
		fireUserEvents = true;
	}
	if (fireUserEvents === true) {
		arg.onBeforeExpand();
	}
	if (jq.buttons.header != null  && jq.buttons.header.length > 0) {
		if (typeof jq.buttons.headerButton !== 'undefined') {
			jq.buttons.headerButton.attr({role: 'button', 'aria-expanded': true});
		} else {
			jq.buttons.header.attr({role: 'button', 'aria-expanded': true});
		}
	}
	if (jq.buttons.content != null && jq.buttons.content.length > 0) {
		jq.buttons.content.attr({role: 'button', 'aria-expanded': false});
	}
	if (arg.style !== 'showMore') {
		jq.content.attr({'aria-hidden': false});
	} else {
		jq.content.children('li').attr({'aria-hidden': false});
	}
	// process styles
	switch(arg.style) {
	case 'alert':
		jq.header.slideUp('fast', function() {
			jq.content.slideDown('fast');
		});
		break;
	case 'simple':
		if (!arg.doesHidden) {
			jq.content.css({display: 'block'});;
		} else {
			jq.content.removeClass('hidden');
		}
		if (arg.isTr) {
			if (tiaacref.isIE().ie) {
				jq.content.css({display: 'block'});
			} else {
				jq.content.css({display: 'table-row'});
			}
		}
		jq.buttons.header.removeClass(style.buttonClass.collapsed).addClass(style.buttonClass.expanded);
		break;
	case 'accountList':
		jq.content.slideDown('fast');
		jq.header.addClass(style.headerClass.expanded).removeClass(style.headerClass.collapsed);
		jq.buttons.header.removeClass(style.buttonClass.collapsed).addClass(style.buttonClass.expanded);
		break;
	case 'panel':
		jq.header.removeClass(style.headerClass.collapsed).addClass(style.headerClass.expanded);
		jq.content.removeClass('hidden ' + style.contentClass.collapsed).addClass(style.contentClass.expanded).css({display: 'block'});;
		jq.buttons.headerButton.removeClass(style.buttonClass.collapsed).addClass(style.buttonClass.expanded);
		break;
	case 'showMore':
		jq.content.children('li').removeClass('hidden');
		jq.header.addClass(style.headerClass.expanded).removeClass(style.headerClass.collapsed).html(style.text.expanded)
			.attr({role: 'button', 'aria-expanded': true, title: 'Select to collapse the content above'});
		jq.header.prepend($('<span>').addClass('icon'));
		break;
	case 'learnMore':
		jq.content.addClass(style.contentClass.expanded).removeClass(style.contentClass.collapsed + 'hidden');
		jq.header.addClass(style.headerClass.expanded).removeClass(style.headerClass.collapsed).html(style.text.expanded)
			.attr({role: 'button', 'aria-expanded': true, title: 'Select to collapse the content below'});
		break;
	case 'transactionList':
		jq.header.children('td').addClass(style.headerClass.expanded).removeClass(style.headerClass.collapsed);
		jq.content.children('td').addClass(style.contentClass.expanded).removeClass(style.contentClass.collapsed);
		jq.content.slideDown('fast');
		break;
	case 'moreHide':
		jq.content.addClass(style.contentClass.expanded).removeClass(style.contentClass.collapsed)
			.attr({'aria-hidden': false});
		jq.header.addClass(style.headerClass.expanded).removeClass(style.headerClass.collapsed)
			.attr({'aria-expanded': true, 'aria-hidden': true});
		if (this.creating === false) {
			jq.buttons.content.focus();
		}
		break;
	default:
		jq.content.css({display: 'block'});
		jq.header.addClass(this.classes.header);
		jq.content.addClass(this.classes.content);
		if (jq.headerToggle !== '') {
			jq.headerToggle.removeClass();
			jq.headerToggle.addClass(this.headerSplit[2]);
		}
		break;
	};
	if (fireUserEvents === true) {
		arg.onAfterExpand();
	}
	this.isExpanded = true;
	tcoipc.fire(this.getExpandEventName(), true);
};

tiaacref.expandCollapse.prototype.collapse = function (fireUserEvents) {
	"use strict";
	var jq = this.jq,
		arg = this.arg,
		style = tiaacref.defaults.expandCollapse.validStyle[arg.style];
	if (typeof fireUserEvents !== 'boolean') {
		fireUserEvents = true;
	}
	if (fireUserEvents === true) {
		arg.onBeforeCollapse();
	}
	if (jq.buttons.header != null  && jq.buttons.header.length > 0) {
		if (typeof jq.buttons.headerButton !== 'undefined') {
			jq.buttons.headerButton.attr({role: 'button', 'aria-expanded': false});
		} else {
			jq.buttons.header.attr({role: 'button', 'aria-expanded': false});
		}
	}
	if (jq.buttons.content != null && jq.buttons.content.length > 0) {
		jq.buttons.content.attr({role: 'button', 'aria-expanded': true});
	}
	if (arg.style !== 'showMore') {
		jq.content.attr({'aria-hidden': true});
	} else {
		jq.content.children('li').attr({'aria-hidden': true}).slice(0, style.collapsedSize).attr({'aria-hidden': false});
	}
	switch (arg.style) {
	case 'alert':
		jq.content.slideUp('fast', function() {
			jq.header.slideDown('fast');
		});
		break;
	case 'simple':
		if (!arg.doesHidden) {
			jq.content.css({display: 'none'});;
		} else {
			jq.content.addClass('hidden');
		}
		if (arg.isTr) {
			jq.content.css({display: ''});
		}
		jq.buttons.header.removeClass(style.buttonClass.expanded).addClass(style.buttonClass.collapsed);
		break;
	case 'accountList':
		jq.content.slideUp('fast');
		jq.header.removeClass(style.headerClass.expanded).addClass(style.headerClass.collapsed);
		jq.buttons.header.removeClass(style.buttonClass.expanded).addClass(style.buttonClass.collapsed);
		break;
	case 'panel':
		jq.header.removeClass(style.headerClass.expanded).addClass(style.headerClass.collapsed);
		jq.content.removeClass(style.contentClass.expanded).addClass('hidden ' + style.contentClass.collapsed).css({display: 'none'});
		jq.buttons.headerButton.removeClass(style.buttonClass.expanded).addClass(style.buttonClass.collapsed);
		break;
	case 'showMore':
		jq.content.children('li').addClass('hidden').slice(0, style.collapsedSize).removeClass('hidden');
		jq.header.removeClass(style.headerClass.expanded).addClass(style.headerClass.collapsed).html(style.text.collapsed)
			.attr({role: 'button', 'aria-expanded': false, title: 'Select to expand the content below'});
		jq.header.prepend($('<span>').addClass('icon'));
		break;
	case 'learnMore':
		jq.content.removeClass(style.contentClass.expanded).addClass(style.contentClass.collapsed + 'hidden');
		jq.header.removeClass(style.headerClass.expanded).addClass(style.headerClass.collapsed).html(style.text.collapsed)
			.attr({role: 'button', 'aria-expanded': false, title: 'Select to expand the content below'});
		break;
	case 'transactionList':
		jq.content.slideUp('fast', function() {
			jq.header.children('td').addClass(style.headerClass.collapsed).removeClass(style.headerClass.expanded);
			jq.content.children('td').addClass(style.contentClass.collapsed).removeClass(style.contentClass.expanded);
		});
		break;
	case 'moreHide':
		jq.content.removeClass(style.contentClass.expanded).addClass(style.contentClass.collapsed)
			.attr({'aria-hidden': true});
		jq.header.removeClass(style.headerClass.expanded).addClass(style.headerClass.collapsed)
			.attr({'aria-expanded': false, 'aria-hidden': false});
		if (this.creating === false) {
			jq.buttons.header.focus();
		}
		break;
	default:
		jq.content.css({display: 'none'});
		jq.header.removeClass(this.classes.header);
		jq.content.removeClass(this.classes.content);
		if (jq.headerToggle !== '') {
			jq.headerToggle.removeClass();
			jq.headerToggle.addClass(this.headerSplit[1]);
		}
		break;
	};
	if (fireUserEvents === true) {
		arg.onAfterCollapse();
	}
	this.isExpanded = false;
	tcoipc.fire(this.getCollapseEventName(), true);
};
tiaacref.expandCollapse.prototype.getHeaderId = function () {
	"use strict";
	return this.arg.header;
};
tiaacref.expandCollapse.prototype.getContentId = function () {
	"use strict";
	return this.arg.content;
};
tiaacref.expandCollapse.prototype.getExpandEventName = function () {
	"use strict";
	return tiaacref.defaults.expandCollapse.expandEventName + this.getHeaderId();
};
tiaacref.expandCollapse.prototype.getCollapseEventName = function () {
	"use strict";
	return tiaacref.defaults.expandCollapse.collapseEventName + this.getHeaderId();
};
tiaacref.expandCollapse.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('expandCollapse.' + eventName) + this.arg.id;
};








/* ============================================= CALENDAR ============================================== */
tiaacref.calendarDates = {};
tiaacref.calendar = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.calendar,
		jq,
		changeSelect,
		yearRange,
		self;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			constrainInput: defaults.constrainInput,
			minDate: defaults.minDate,
			style: defaults.style,
			selectedDate: defaults.selectedDate,
			setState: defaults.setState,
			maxDate: defaults.maxDate,
			calendarOptions: defaults.calendarOptions,
			disableWeekends: defaults.disableWeekends,
			showAssistText: defaults.showAssistText
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.constrainInput !== 'boolean') {
			arg.constrainInput = defaults.constrainInput;
		}
		if (typeof arg.minDate !== 'string') {
			arg.minDate = defaults.minDate;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof arg.selectedDate !== 'string') {
			arg.selectedDate = defaults.selectedDate;
		}
		if (typeof arg.maxDate !== 'string' && arg.maxDate !== null) {
			arg.maxDate = defaults.maxDate;
		}
		if (typeof arg.setState !== 'object' || arg.setState === {}) {
			arg.setState = defaults.setState;
		}
		if (typeof arg.setState.state !== 'string' || typeof arg.setState.dates !== 'string') {
			arg.setState = defaults.setState;
		}
		if (typeof defaults.validState[arg.setState.state] === 'undefined') {
			arg.setState = defaults.setState;
		}
		if (typeof arg.showAssistText !== 'boolean') {
			arg.showAssistText = defaults.showAssistText;
		}
		if (typeof arg.disableWeekends !== 'boolean') {
			arg.disableWeekends = defaults.disableWeekends;
		}
		if (typeof arg.calendarOptions !== 'object') {
			arg.calendarOptions = defaults.calendarOptions;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'calendar');
	}
	if (typeof defaults.validStyle[arg.style] === 'undefined') {
		arg.style = defaults.style;
	}
	arg.assistText = (arg.selectedDate != null ? arg.selectedDate : defaults.assistText);
	this.arg = arg;
	this.jq = jq;
	this.dates = {};
	self = this;
	changeSelect = (arg.style === 'monthyearselect' ? true : false);
	tcoipc.on(this.getSelectDateEventName(), function(payload) {
		"use strict";
		if (typeof payload === 'string' || payload instanceof Date) {
			self.jq.id.datepicker('setDate', new Date(payload));
		}
	});
	this.setState(arg.setState);
	yearRange = (typeof arg.calendarOptions.yearRange !== 'string' ? 'c-10:c+10' : arg.calendarOptions.yearRange);
	jq.id.datepicker({
		constrainInput: arg.constrainInput,
		minDate: arg.minDate,
		maxDate: arg.maxDate,
		changeMonth: changeSelect,
		changeYear: changeSelect,
		defaultDate: arg.selectedDate,
		yearRange: yearRange,
		onSelect: function (dateText, inst) {
			"use strict";
			tcoipc.fire(self.getSelectDateEventName(), {text: dateText, date: inst});
		},
		beforeShow: function() {
			"use strict";
			if (self.arg.showAssistText) { 
				if (self.jq.id.val() === self.arg.assistText) {
					self.jq.id.val('');
				}
			}
			return;
		},
		onClose: function() {
			"use strict";
			if (self.arg.showAssistText) {
				if (self.jq.id.val() === '') {
					self.jq.id.val(self.arg.assistText);
				}
			}
		},
		beforeShowDay: function (theDate) {
			"use strict";
			return self.setStateOnDate(theDate);
		},
		onChangeMonthYear: function (year, month, inst) {
			"use strict";
			tcoipc.fire(self.getChangeMonthYearEventName(), {year: year, month: month, date: inst});
		}
	});
	if (arg.showAssistText) {
		jq.id.val(self.arg.assistText);
	}
};
tiaacref.calendar.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};
tiaacref.calendar.prototype.getSelectDateEventName = function () {
	"use strict";
	return tiaacref.defaults.calendar.events.selectDateName + this.arg.id;
};
tiaacref.calendar.prototype.getShowDayEventName = function () {
	"use strict";
	return tiaacref.defaults.calendar.events.showDayName + this.arg.id;
};
tiaacref.calendar.prototype.getChangeMonthYearEventName = function () {
	"use strict";
	return tiaacref.defaults.calendar.events.changeMonthYearName + this.arg.id;
};
tiaacref.calendar.prototype.getMonths = function (type) {
	"use strict";
	var defaults = tiaacref.defaults.calendar;
	if (typeof type !== 'string') {
		type = defaults.monthsType;
	}
	if (typeof defaults.validMonthsType[type] === 'undefined') {
		type = defaults.monthsType;
	}
	if (type === 'long') {
		return this.jq.id.datepicker('option', 'monthNames');
	} else if (type === 'short') {
		return this.jq.id.datepicker('option', 'monthNamesShort');
	}
	
};
tiaacref.calendar.prototype.setState = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.calendar,
		theDates,
		i,
		aDate,
		rangeOfDates,
		lowDate,
		highDate,
		oneDay = 1000 * 60 * 60 * 24,
		id = this.arg.id;
	if (typeof arg !== 'object') {
		throw tiaacref.error('', 'Argument for calendar.setState() not object.');
	}
	if (typeof arg.state !== 'string' || typeof arg.dates !== 'string') {
		throw tiaacref.error('', 'Argument for calendar.setState() does not contain string properties arg.state and arg.dates.');
	}
	if (typeof defaults.validState[arg.state] === 'undefined') {
		throw tiaacref.error('', 'Argument property arg.state for calendar.setState() is not valid.');
	}
	if (arg.dates !== '') {
		theDates = arg.dates.split(',');
		for(i in theDates) {
			rangeOfDates = theDates[i].split('-');
			if (rangeOfDates.length > 1) {
				lowDate = new Date(rangeOfDates[0]);
				highDate = new Date(rangeOfDates[1]);
				if (tiaacref.isValidDate(lowDate) && tiaacref.isValidDate(highDate)) {
					while (lowDate.getTime() <= highDate.getTime()) {
						if (typeof tiaacref.calendarDates[id] === 'undefined') {
							tiaacref.calendarDates[id] = {};
						}
						tiaacref.calendarDates[id][tiaacref.serializeDate(lowDate)] = defaults.validState[arg.state];
						lowDate = new Date(lowDate.getTime() + oneDay);
					}
				}
			} else {
				aDate = new Date(theDates[i]);
				if (tiaacref.isValidDate(aDate)) {
					if (typeof tiaacref.calendarDates[id] === 'undefined') {
						tiaacref.calendarDates[id] = {};
					}
					tiaacref.calendarDates[id][tiaacref.serializeDate(aDate)] = defaults.validState[arg.state];
				}
			}
		}
	}
	return;
};
tiaacref.calendar.prototype.setStateOnDate = function (theDate) {
	"use strict";
	var defaults = tiaacref.defaults.calendar,
		sDate = tiaacref.serializeDate(theDate),
		id = this.arg.id;
	if (typeof tiaacref.calendarDates[id] === 'undefined') {
		tiaacref.calendarDates[id] = {};
	}
	if (typeof tiaacref.calendarDates[id][sDate] === 'undefined') {
		tiaacref.calendarDates[id][sDate] = defaults.validState.normal;
	}
	if (this.arg.disableWeekends && (theDate.getDay() == 0 || theDate.getDay() == 6)) {
		tiaacref.calendarDates[id][sDate] = defaults.validState.disabled;
	}
	return tiaacref.calendarDates[id][sDate];
};

tiaacref.calendar.prototype.refresh = function () {
	"use strict";
	this.jq.id.datepicker('refresh');
};

tiaacref.calendar.prototype.setMaxDate = function (maxDate) {
	"use strict";
	this.jq.id.datepicker('option', 'maxDate', maxDate);
	return;
};
tiaacref.calendar.prototype.setMinDate = function (minDate) {
	"use strict";
	this.jq.id.datepicker('option', 'minDate', minDate);
	return;
};
tiaacref.calendar.prototype.getAssistText = function () {
	"use strict";
	return this.jq.arg.assistText;
};
tiaacref.calendar.prototype.setAssistText = function(text) {
	"use strict";
	if (typeof text !== 'string') {
		throw tiaacref.error('badId', text, 'calendar.setAssistText');
	}
	this.jq.arg.assistText = text;
	return;
};
tiaacref.calendar.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('calendar.' + eventName) + this.arg.id;
};






/* ============================================= AUTO COMPLETE ======================================= */
tiaacref.autoComplete = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.autoComplete,
		jq,
		self,
		dataParameters,
		i;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			data: defaults.data,
			minimumLength: defaults.minimumLength,
			onSelect: defaults.onSelect,
			dataType: defaults.dataType,
			serverParameters: defaults.serverParamters
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.data === 'undefined') {
			arg.data = defaults.data;
		}
		if (!arg.data instanceof Array) {
			arg.data = defaults.data;
		}
		if (typeof arg.minimumLength === 'string') {
			arg.minimumLength = parseFloat(arg.minimumLength);
		}
		if (typeof arg.minimumLength !== 'number') {
			arg.minimumLength = defaults.minimumLength;
		}
		if (typeof arg.onSelect !== 'function') {
			arg.onSelect = defaults.onSelect;
		}
		if (typeof arg.dataType !== 'string') {
			arg.dataType = defaults.dataType;
		}
		if (typeof defaults.validDataType[arg.dataType] === 'undefined') {
			arg.dataType = defaults.dataType;
		}
		if (typeof arg.serverParameters !== 'object') {
			arg.serverParameters = defaults.serverParameters;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.missingDOM('badId', arg.id, 'autoComplete');
	}
	this.jq = jq;
	this.arg = arg;
	self = this;
	jq.id.autocomplete({
		minLength: arg.minimumLength,
		source: function(request, response) {
			dataParameters = tiaacref.clone(arg.serverParameters);
			for (i in arg.serverParameters) {
				if (dataParameters[i] == 'tcoRequestTerm') {
					dataParameters[i] = request.term; 
				}
			}
			tcoRequestTerm = request.term;
			if (typeof arg.data == 'object') {
				_fireEvent("success", {}, {}, arg.data, response);
			} else {
				$.ajax({
					url: arg.data,
					dataType: arg.dataType,
					data: dataParameters,
					statusCode: {
						404: function(jqXHR, textStatus, errorThrown, response) {
							_fireEvent("error", jqXHR, textStatus, errorThrown);
						}
					},
					success: function(data, textStatus, jqXHR) {
						_fireEvent("success", jqXHR, textStatus, data, response);
					},
					error: function(jqXHR, textStatus, errorThrown) {
						_fireEvent("error", jqXHR, textStatus, errorThrown, response);
					}
				});
			}
		},
		select: function(event,ui){
			tcoipc.fire(self.getSelectEventName(), {event: event, ui: ui});
			self.arg.onSelect(event,ui);
		}
	});
	
	function _fireEvent(type, jqXHR, status, data, response) {
		"use strict";
		var event = (type === 'success' ? self.getSuccessEventName() : self.getErrorEventName()),
			data = (type === 'success' ? data : null);
		response(data);
		tcoipc.fire(event, {response: jqXHR, status: status, data: data});
		return;
	}
	
};
tiaacref.autoComplete.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};
tiaacref.autoComplete.prototype.getSelectEventName = function () {
	"use strict";
	return tiaacref.eventName('autoComplete.select') + this.getId();
};
tiaacref.autoComplete.prototype.getErrorEventName = function () {
	"use strict";
	return tiaacref.eventName('autoComplete.error') + this.getId();
};
tiaacref.autoComplete.prototype.getSuccessEventName = function () {
	"use strict";
	return tiaacref.eventName('autoComplete.success') + this.getId();
};
tiaacref.autoComplete.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('autoComplete.' + eventName) + this.arg.id;
};

// the following is used as a global reserved variable that represents the 'request.term'
// variable in the 'source' argument above for the autocomplete. That is, it is the 
// reqeusted data coming in from the text box.
// The programmer can reference it in constructing the component.
var tcoRequestTerm = '';










/* ============================================ TABLE SORT ================================================== */
tiaacref.tableSort = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.tableSort,
		jq,
		sortHeaders,
		self,
		sortName,
		initialSort,
		thSortName,
		multipleSortNames = 0,
		duplicateSortName = {},
		trSortRow;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			onBeforeSort: defaults.onBeforeSort,
			onAfterSort: defaults.onAfterSort,
			multiSort: defaults.multiSort,
			caseSensitive: defaults.caseSensitive,
			initialSort: defaults.initialSort,
			multiColumnSort: defaults.multiColumnSort
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.onBeforeSort !== 'function') {
			arg.onBeforeSort = defaults.onBeforeSort;
		}
		if (typeof arg.onAfterSort !== 'function') {
			arg.onAfterSort = defaults.onAfterSort;
		}
		if (typeof arg.multiSort !== 'boolean') {
			arg.multiSort = defaults.multiSort;
		}
		if (typeof arg.caseSensitive !== 'boolean') {
			arg.caseSensitive = defaults.caseSensitive;
		}
		if (typeof arg.initialSort !== 'string') {
			arg.initialSort = defaults.initialSort;
		}
		if (typeof arg.multiColumnSort !== 'boolean') {
			arg.multiColumnSort = defaults.multiColumnSort;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'tableSort');
	}
	// prevents extra assignments if tablesort within a tablesort
	if (jq.id.attr('data-sorttable')) {
		return;
	}
	// check for multiple <tr>s. Only one <tr> may have data-sortname
	sortHeaders = null;
	jq.id.find('thead:first tr').each(function () {
		var tr = $(this).children('th');
		if (tr.filter('[data-sortname]').length > 0) {
			multipleSortNames ++;
			sortHeaders = tr;
		}
	});
	if (multipleSortNames > 1) {
		throw tiaacref.error('', 'Multiple sort columns (data-sortname=xxxx) defined in multiple rows. You can only define sortcolumns in one row.');
	}
	if (sortHeaders == null) {
		throw tiaacref.error('', 'No sort columns (data-sortname=xxxx) could be found for arg.id in tableSort.');
	}
	if (sortHeaders.filter('[data-sortname]').length === 0) {
		throw tiaacref.error('', 'No sort columns (data-sortname=xxxx) could be found for arg.id in tableSort.');
	}
	jq.id.attr({'data-sorttable': true});
	this.arg = arg;
	this.jq = jq;
	this.sortTable = [];
	this.multiSortTable = [];
	this.sortParameters = [];
	this.filterParameters = [];
	this.moreHideParameters = [];
	self = this;
	sortHeaders.each(function (thIndex) {
		var thHeader = $(this),
			filter,
			moreHide = thHeader.attr('data-sortmorehide');
		if (thHeader.filter('[data-sortname]').length > 0) {
			thHeader.attr({tabindex: 0});
			thSortName = thHeader.attr('data-sortname');
			self.sortParameters.push({
				index: thIndex,
				name: thSortName,
				dataType: thHeader.attr('data-sortdatatype'),
				format: thHeader.attr('data-sortformat')
			});
			if (thSortName != undefined) {
				if (typeof duplicateSortName[thSortName] !== 'undefined') {
					console.log("WARNING: Duplicate data-sortnames found (name = " + thSortName + ", table = " + self.arg.id + ")");
				}
				duplicateSortName[thSortName] = true;
			}
		}
		// data morehide
		if (moreHide != 'undefined') {
			self.moreHideParameters.push({
				index: thIndex,
				number: (tiaacref.isNumber(moreHide) ? parseInt(moreHide) : defaults.moreHideSize)
			});
		}
		// data filters
		if (thHeader.filter('[data-sortfilter="true"]').length > 0) {
			if (thHeader.find('select').length === 0) {
				filter = $('<input>').attr({
					size: "5",
					type: "text",
					'data-index': thIndex,
					id: self.arg.id + 'filter' + thIndex,
					name: self.arg.id + 'filter' + thIndex
				});
				filter.on('click', function(e) {
					e.stopPropagation();
					return;
				});
				filter.on('keyup', function(e) {
					doFilter(e);
				});
				filter.on('keydown', function (e) {
					e.stopPropagation();
				});
				filter.on('change', function(e) {
					doFilter(e);
				});
				thHeader.append($('<br>')).append(filter);
				self.filterParameters.push({id: filter, dataIndex: thIndex, dataName: thHeader.attr('data-sortname')});
			} else {
				filter = thHeader.find('select').first();
				self.filterParameters.push({id: filter, dataIndex: thIndex, dataName: thHeader.attr('data-sortname')});
				filter.on('click', function(event) {
					event.stopPropagation();
				});
				filter.on('change', {filter: filter}, function(event) {
					doFilter(event);
				});
				filter.on('keydown', function (e) {
					e.stopPropagation();
				});
			}
		}
		if (thHeader.attr('data-sort') == undefined) {
			thHeader.attr({'data-sort': false});
		}
	});
	tcoipc.on(self.event('applyFilter'), function(payload) {
		doFilter();
	});
	function doFilter(event) {
		var searchText,
			dataIndex,
			searchName,
			filterArguments = [],
			regExModifier = self.arg.caseSensitive === true ? "" : "i",
			sortTableRecord,
			filterTableRecord,
			selectRecord,
			i,
			j,
			recordCount,
			totalRecordCount;
		selectRecord = true;
		recordCount = 0;
		totalRecordCount = 0;
		for (i in self.sortTable) {
			sortTableRecord = self.sortTable[i];
			selectRecord = true;
			for (j in self.filterParameters) {
				filterTableRecord = self.filterParameters[j];
				searchText = filterTableRecord.id.val();
				dataIndex = filterTableRecord.dataIndex;
				if (typeof searchText !== "undefined" && searchText !== '') {
					if (totalRecordCount === 0) {
						filterArguments.push({searchText: searchText, dataIndex: dataIndex, dataName: filterTableRecord.dataName});
					}
					if (sortTableRecord[dataIndex].search(new RegExp(tiaacref.regExpEscape(searchText), regExModifier)) !== -1) {
						selectRecord = (selectRecord === true ? true : false);
					} else {
						selectRecord = false;
					}
				}
			}
			if (selectRecord === true) {
				sortTableRecord.filtered = false;
				sortTableRecord.id.removeClass('hidden').attr({'data-sortfiltered': false});
				for (j in sortTableRecord.multiSort) {
					sortTableRecord.multiSort[j].id.removeClass('hidden');
				}
				recordCount ++;
			} else {
				sortTableRecord.filtered = true;
				sortTableRecord.id.addClass('hidden').attr({'data-sortfiltered': true});
				for (j in sortTableRecord.multiSort) {
					sortTableRecord.multiSort[j].id.addClass('hidden');
				}
			}
			totalRecordCount ++;
		}
		tcoipc.fire(self.event('filter'), filterArguments);
		tcoipc.fire(tiaacref.eventName('paginate.setTableNumRecords') + self.arg.id, recordCount);
		tcoipc.fire(tiaacref.eventName('paginate.drawPagination') + self.arg.id, {data: 1});
		tcoipc.fire(tiaacref.eventName('paginate.recalcDataSource') + self.arg.id, {data: 1});
		tcoipc.fire(tiaacref.eventName('paginate.drawTable') + self.arg.id, {data: 1});
	}
	this.sortHeaders = sortHeaders.filter('[data-sortname]');
	this.sortHeaders
		.addClass('header')
		.attr({
			'data-sortpath': 'none'
		});
	this.reloadSortData();
	// assign clicks
	this.sortHeaders.each(function(index) {
		if ($(this).attr('data-sort') == 'true') {
			$(this).on({
				click: function (e) {
				e.stopPropagation();
					_toggleSort(this, self);
				return false;
				},
				keydown: function (e) {
					e.stopPropagation();
					if (e.keyCode === 13) {
						_toggleSort(this, self);
					}
				}
			});
		} else {
			$(this).removeClass('header');
		}
	});
	
	tcoipc.on(this.event('reloadSortData'), function(payload) {
		"use strict";
		self.reloadSortData();
	});
	// initial sort
	if (arg.initialSort !== '') {
		initialSort = arg.initialSort.split('/');
		if (initialSort.length === 1) {
			initialSort.push(defaults.initialSortDirection);
		}
		this.sort(initialSort[0], initialSort[1]);
	}
	// events
	tcoipc.on(this.event('applySort'), function(payload) {
		self.sortMulti(payload, true);
	});
	function _toggleSort(thisHeader, self) {
		var sortHeader = $(thisHeader),
			dataSortPath = sortHeader.attr('data-sortpath');
		if (dataSortPath === 'none') {
			dataSortPath = 'asc';
		} else if (dataSortPath === 'asc') {
			dataSortPath = 'desc';
		} else {
			dataSortPath = 'asc';
		}
		self.sort(sortHeader.attr('data-sortname'), dataSortPath);
	return;
	}

	return;
};

tiaacref.tableSort.prototype.attachFilter = function (element, columnName) {
	if (typeof element !== 'string' || typeof columnName !== 'string') {
		throw new TypeError('Both arguments for tableSort.attachFilter() must be strings.');
	}
	var attachElement = $(element);
	if (attachElement.length === 0) {
		throw new ReferenceError("'element' argument for tableSort.attachFilter() must be a valid DOM selector.");
	}
	var column = -1, self = this;
	this.sortHeaders.each(function (index, element) {
		var thColumn = $(element);
		if (thColumn.attr('data-sortname') === columnName) {
			column = index;
			self.filterParameters.push({id: attachElement, dataIndex: index, dataName: thColumn.attr('data-sortname')});
		}
	});
	if (column === -1) {
		throw new ReferenceError("'columnName' argument for tableSort.attachFilter() must be a valid column name within the table.");
	}
	attachElement.off('click.tableSort').on('click.tableSort', function(e) {
		e.stopPropagation();
		return;
	});
	attachElement.off('keyup.tablesort').on('keyup.tableSort', function(e) {
		tcoipc.fire(self.event('applyFilter'), null);
	});
	attachElement.off('keydown.tableSort').on('keydown.tableSort', function (e) {
		e.stopPropagation();
	});
	attachElement.off('change.tableSort').on('change.tableSort', function(e) {
		tcoipc.fire(self.event('applyFilter'), null);
	});
};
// -1: s1 < s2
// 0 : s1 == s2
// +1: s1 > s2
tiaacref.tableSort.prototype.strcmp = function (s1, s2) {
	"use strict";
	var retval = 0,
		j,
		i;
	if (typeof s1 !== 'string') {
		return false;
	}
	if (typeof s2 !== 'string') {
		return false;
	}
	if (!this.arg.caseSensitive) {
		s1 = s1.toUpperCase();
		s2 = s2.toUpperCase();
	}
	if (s1 === '' && s2 === '') {
		return 0;
	}
	if (s1 === '') {
		return -1;
	}
	if (s2 === '') {
		return 1;
	}
	j = Math.min(s1.length, s2.length);
	i = 0;
	while (i < j && retval === 0) {
		if (s1.substr(i, 1) < s2.substr(i, 1)) {
			retval = -1;
		} else if (s1.substr(i, 1) > s2.substr(i, 2)) {
			retval = 1;
		}
		i++;
	}
	if (retval === 0) {
		if (s1.length < s2.length) {
			retval = -1;
		} else if (s1.length > s2.length) {
			retval = 1;
		}
	}
	return retval;
};
tiaacref.tableSort.prototype.numcmp = function (s1, s2) {
	"use strict";
	var retval = 0,
		n1,
		n2;
	if (typeof s1 !== 'string') {
		return false;
	}
	if (typeof s2 !== 'string') {
		return false;
	}
	if (s1 === '' && s2 === '') {
		return 0;
	}
	if (s1 === '') {
		return -1;
	}
	if (s2 === '') {
		return 1;
	}
	n1 = parseFloat(s1);
	n2 = parseFloat(s2);
	if (n1 < n2) {
		return -1;
	}
	if (n1 > n2) {
		return 1;
	}
	return 0;
};

tiaacref.tableSort.prototype.datecmp = function (d1, d2) {
	"use strict";
	var newD1,
		newD2;
	if (typeof d1 !== 'string') {
		return false;
	}
	if (typeof d2 !== 'string') {
		return false;
	}
	if (d1 === '' && d2 === '') {
		return 0;
	}
	if (d1 === '') {
		return -1;
	}
	if (d2 === '') {
		return 1;
	}
	newD1 = (new Date(d1)).getTime();
	newD2 = (new Date(d2)).getTime();
	if (newD1 < newD2) {
		return -1;
	} else if (newD1 > newD2) {
		return 1;
	}
	return 0; 
};

tiaacref.tableSort.prototype.curcmp = function (currency1, currency2, currencyType) {
	"use strict";
	var retval;
	if (typeof currencyType !== 'string') {
		currencyType = "us";
	}
	if (typeof currency1 !== 'string') {
		return false;
	}
	if (typeof currency2 !== 'string') {
		return false;
	}
	if (currency1 === '' && currency2 === '') {
		return 0;
	}
	if (currency1 === '') {
		return -1;
	}
	if (currency2 === '') {
		return 1;
	}
	switch (currencyType) {
	case "us":
	default:
		currency1 = currency1.replace(/\$/g, "").replace(/,/g, "");
		currency2 = currency2.replace(/\$/g, "").replace(/,/g, "");
		retval = this.numcmp(currency1, currency2);
		break;
	}
	return retval;
};


tiaacref.tableSort.prototype.sortMulti = function (columns, fireUserEvents) {
	"use strict";
	var index = [],
		dataType = [],
		tempDataType,
		dataSortPath = [],
		headerSortClass = [],
		sortTable = [],
		h,
		i,
		j,
		l,
		m,
		oldBody,
		compareResult,
		t,
		sortOrderNumber,
		sortTableRecord;
	if (columns instanceof Array == false) {
		throw tiaacref.error('', 'columns not an array in tableSort.sortMulti');
	}
	m = columns.length;
	l = this.sortParameters.length;
	for (h = 0; h < m; h++) {
		for (i = 0; i < l; i++) {
			if (this.sortParameters[i].name === columns[h].name) {
				index.push(this.sortParameters[i].index);
				tempDataType = this.sortParameters[i].dataType;
				if (typeof tempDataType !== 'string') {
					tempDataType = "string";
				}
				tempDataType = tempDataType.split(':');
				if (tempDataType.length === 1) {
					tempDataType.push("us");
				}
				dataType.push(tempDataType);
			}
		}
	}
	m = index.length;
	if (index === []) {
		throw tiaacref.error('', 'columns.name not found in tableSort.sortMulti');
	}
	for (h = 0; h < m; h++) {
		if (columns[h].direction === 'asc') {
			dataSortPath.push('asc');
			headerSortClass.push('headerSortUp');
		} else if (columns[h].direction === 'desc') {
			dataSortPath.push('desc');
			headerSortClass.push('headerSortDown');
		} else {
			dataSortPath.push('desc');
			headerSortClass.push('headerSortDown');
		}
	}
	sortOrderNumber = this.sortHeaders.find('span[data-sortordernumber="true"]');
	if (sortOrderNumber.length > 0) {
		this.sortHeaders.removeClass('headerSortUp headerSortDown').attr({'data-sortpath': 'none'});
		sortOrderNumber.text("");
	} else {
		this.sortHeaders.removeClass('headerSortUp headerSortDown').attr({'data-sortpath': 'none'}).children('span.txtlabel').remove();
	}
	l = this.sortHeaders.length;
	for (h = 0; h < m; h ++) {
		for (i = 0; i < l; i++) {
			j = $(this.sortHeaders[i]);
			if (j.attr('data-sortname') === columns[h].name) {
				j.addClass(headerSortClass[h]).attr({'data-sortpath': dataSortPath[h]});
				if (this.arg.multiColumnSort === true) {
					sortOrderNumber = j.find('span[data-sortordernumber="true"]');
					if (sortOrderNumber.length === 1) {
						sortOrderNumber.text(h + 1);
					} else {
						j.append('<span class="txtlabel flr">' + (h + 1) + '</span>');
					}
				}
			}
		}
	}
	
//	return;
	if (typeof fireUserEvents !== 'boolean') {
		fireUserEvents = true;
	}
	// TODO: get better sort routine. A first grader can do better.
	sortTable = tiaacref.clone(this.sortTable);	
	if (fireUserEvents === true) {
		this.arg.onBeforeSort();
	}
	tcoipc.fire(this.getBeforeSortEventName(), columns);
	l = sortTable.length;
	console.log(index);
	console.log(m);
	var supercount = 0;
	while (l > 1) {
		for (i = 0, l = l - 1; i < l; i++) {
			h = 0;
			while (h < m) {
				if (h > 0) {
					supercount++;
					if (supercount<10) {
						console.log('START');
						console.log(sortTable[i][index[h]]);
						console.log(sortTable[i + 1][index[h]]);
					}
				}
				tempDataType = dataType[h];
				if (tempDataType[0] === 'string') {
					compareResult = this.strcmp(sortTable[i][index[h]], sortTable[i + 1][index[h]]);
					if ((compareResult === -1 && columns[h].direction === 'desc') || (compareResult === 1 && columns[h].direction === 'asc')) {
						t = sortTable[i];
						sortTable[i] = sortTable[i + 1];
						sortTable[i + 1] = t;
					} 
					if (compareResult !== 0) {
						h = m;
					}
				} else if (tempDataType[0] === 'number') {
					compareResult = this.numcmp(sortTable[i][index[h]], sortTable[i + 1][index[h]]);
					if ((compareResult === -1 && columns[h].direction === 'desc') || (compareResult === 1 && columns[h].direction === 'asc')) {
						t = sortTable[i];
						sortTable[i] = sortTable[i + 1];
						sortTable[i + 1] = t;
						h = m;
					}
					if (compareResult !== 0) {
						h = m;
					}
				} else if (tempDataType[0] === 'date') {
					compareResult = this.datecmp(sortTable[i][index[h]], sortTable[i + 1][index[h]]);
					if ((compareResult === -1 && columns[h].direction === 'desc') || (compareResult === 1 && columns[h].direction === 'asc')) {
						t = sortTable[i];
						sortTable[i] = sortTable[i + 1];
						sortTable[i + 1] = t;
						h = m;
					}
					if (compareResult !== 0) {
						h = m;
					}
				} else if (tempDataType[0] === 'currency') {
					compareResult = this.curcmp(sortTable[i][index[h]], sortTable[i + 1][index[h]], tempDataType[1]);
					if ((compareResult === -1 && columns[h].direction === 'desc') || (compareResult === 1 && columns[h].direction === 'asc')) {
						t = sortTable[i];
						sortTable[i] = sortTable[i + 1];
						sortTable[i + 1] = t;
						h = m;
					}
					if (compareResult !== 0) {
						h = m;
					}
				}
				h ++;
			}
		}
	}
	oldBody = this.jq.id.find('tbody').first();
	// what needs to be done here is that if multisort is true, then the <tr> that appears after the number
	// are appended until  another multisort is encountered OR the end of the table is found.
	for (i = 0, l = sortTable.length; i < l; i++) {
		sortTableRecord = sortTable[i].id;
		if (!this.arg.multiSort) {
			oldBody.append(sortTableRecord);
		} else {
			oldBody.append(sortTableRecord);
			for (j in sortTable[i].multiSort) {
				oldBody.append(sortTable[i].multiSort[j].id);
			}
		}
	}
	if (fireUserEvents === true) {
		this.arg.onAfterSort();
	}
	tcoipc.fire(this.getSortEventName(), columns);
	tcoipc.fire(this.getAfterSortEventName(), columns);
	tcoipc.fire(tiaacref.eventName('paginate.recalcDataSource') + this.arg.id, {data: 1});
//return;	
	tcoipc.fire(tiaacref.eventName('paginate.drawTable') + this.arg.id, {data: 1});
	return;
};








tiaacref.tableSort.prototype.sort = function (columnName, sortOrder, fireUserEvents) {
	"use strict";
	var index = -1,
		dataType = '',
		dataSortPath,
		headerSortClass,
		sortTable = [],
		i,
		j,
		l,
		oldBody,
		compareResult,
		t,
		sortOrderNumber,
		sortTableRecord;
	if (typeof columnName !== 'string') {
		throw tiaacref.error('', 'columnName not speficied in tableSort.sort');
	}
	if (typeof sortOrder !== 'string') {
		throw tiaacref.error('', 'sortOrder not speficied in tableSort.sort');
	}
	if (sortOrder !== 'asc' && sortOrder !== 'desc') {
		throw tiaacref.error('', 'sortOrder not "asc" nor "desc" in tableSort.sort');
	}
	for (i = 0, l = this.sortParameters.length; i < l; i++) {
		if (this.sortParameters[i].name === columnName) {
			index = this.sortParameters[i].index;
			dataType = this.sortParameters[i].dataType;
			if (typeof dataType !== "string") {
				dataType = "string";
			}
			dataType = dataType.split(':');
			if (dataType.length === 1) {
				dataType.push("us");
			}
		}
	}
	if (index === -1) {
		throw tiaacref.error('', 'columnName not found in tableSort.sort');
	}
	
	if (sortOrder === 'asc') {
		dataSortPath = 'asc';
		headerSortClass = 'headerSortUp';
	} else if (sortOrder === 'desc') {
		dataSortPath = 'desc';
		headerSortClass = 'headerSortDown';
	} else {
		dataSortPath = 'desc';
		headerSortClass = 'headerSortDown';
	}
	sortOrderNumber = this.sortHeaders.find('span[data-sortordernumber="true"]');
	if (sortOrderNumber.length > 0) {
		this.sortHeaders.removeClass('headerSortUp headerSortDown').attr({'data-sortpath': 'none'});
		sortOrderNumber.text("");
	} else {
		this.sortHeaders.removeClass('headerSortUp headerSortDown').attr({'data-sortpath': 'none'}).children('span.txtlabel').remove();
	}
	l = this.sortHeaders.length;
	for (i = 0; i < l; i++) {
		j = $(this.sortHeaders[i]);
		if (j.attr('data-sortname') === columnName) {
			j.addClass(headerSortClass).attr({'data-sortpath': dataSortPath});
			if (this.arg.multiColumnSort === true) {
				sortOrderNumber = j.find('span[data-sortordernumber="true"]');
				if (sortOrderNumber.length === 1) {
					sortOrderNumber.text("1");
				} else {
					j.append('<span class="txtlabel flr">1</span>');
				}
			}
		}
	}
	
	
	
	if (typeof fireUserEvents !== 'boolean') {
		fireUserEvents = true;
	}
	// TODO: get better sort routine. A first grader can do better.
	sortTable = tiaacref.clone(this.sortTable);	
	if (fireUserEvents === true) {
		this.arg.onBeforeSort();
	}
	tcoipc.fire(this.getBeforeSortEventName(), {columnName: columnName, sortOrder: sortOrder});
	l = sortTable.length;
	while (l > 1) {
		for (i = 0, l = l - 1; i < l; i++) {
			if (dataType[0] === 'string') {
				compareResult = this.strcmp(sortTable[i][index], sortTable[i + 1][index]);
				if ((compareResult === -1 && sortOrder === 'desc') || (compareResult === 1 && sortOrder === 'asc')) {
					t = sortTable[i];
					sortTable[i] = sortTable[i + 1];
					sortTable[i + 1] = t;
				}
			} else if (dataType[0] === 'number') {
				compareResult = this.numcmp(sortTable[i][index], sortTable[i + 1][index]);
				if ((compareResult === -1 && sortOrder === 'desc') || (compareResult === 1 && sortOrder === 'asc')) {
					t = sortTable[i];
					sortTable[i] = sortTable[i + 1];
					sortTable[i + 1] = t;
				}
			} else if (dataType[0] === 'date') {
				compareResult = this.datecmp(sortTable[i][index], sortTable[i + 1][index]);
				if ((compareResult === -1 && sortOrder === 'desc') || (compareResult === 1 && sortOrder === 'asc')) {
					t = sortTable[i];
					sortTable[i] = sortTable[i + 1];
					sortTable[i + 1] = t;
				}
			} else if (dataType[0] === 'currency') {
				compareResult = this.curcmp(sortTable[i][index], sortTable[i + 1][index], dataType[1]);
				if ((compareResult === -1 && sortOrder === 'desc') || (compareResult === 1 && sortOrder === 'asc')) {
					t = sortTable[i];
					sortTable[i] = sortTable[i + 1];
					sortTable[i + 1] = t;
				}
			}
		}
	}
	oldBody = this.jq.id.find('tbody').first();
	// what needs to be done here is that if multisort is true, then the <tr> that appears after the number
	// are appended until  another multisort is encountered OR the end of the table is found.
	for (i = 0, l = sortTable.length; i < l; i++) {
		sortTableRecord = sortTable[i].id;
		if (!this.arg.multiSort) {
			oldBody.append(sortTableRecord);
		} else {
			oldBody.append(sortTableRecord);
			for (j in sortTable[i].multiSort) {
				oldBody.append(sortTable[i].multiSort[j].id);
			}
		}
	}
	if (fireUserEvents === true) {
		this.arg.onAfterSort();
	}
	tcoipc.fire(this.getSortEventName(), {columnName: columnName, sortOrder: sortOrder});
	tcoipc.fire(this.getAfterSortEventName(), {columnName: columnName, sortOrder: sortOrder});
	tcoipc.fire(tiaacref.eventName('paginate.recalcDataSource') + this.arg.id, {data: 1});
//return;	
	tcoipc.fire(tiaacref.eventName('paginate.drawTable') + this.arg.id, {data: 1});
	return;
};

tiaacref.tableSort.prototype.reloadSortData = function () {
	"use strict";
	var defaults = tiaacref.defaults.tableSort,
		self = this,
		trSortRow = -1;
	self.multiSortTable = [];
	self.sortTable = [];
	self.jq.id.find('tbody:first > tr').each(function (trIndex) {
		var trObject = $(this),
			sortFiltered = trObject.attr('data-sortfiltered'),
			rowObject = {
				id: trObject,
				index: trIndex,
				multiSort: []
			};
		if (self.arg.multiSort === true && trObject.attr('data-sortmultisort') === true) {
			trObject.attr({'data-sortfiltered': (sortFiltered == undefined ? false : sortFiltered)});
		}
		trObject.find('td').each(function (tdIndex) {
			var tdObject = $(this),
				attr = tdObject.attr('data-sortvalue');
			if (attr === undefined || attr === '') {
				rowObject[tdIndex] = tdObject.text();
			} else {
				rowObject[tdIndex] = tdObject.attr('data-sortvalue');
			}
		});
		if (!self.arg.multiSort) {
			self.sortTable.push(rowObject);
			trSortRow ++;
		} else {
			if (trObject.attr('data-sortmultisort') === 'true') {
				self.sortTable.push(rowObject);
				trSortRow ++;
			} else {
				self.multiSortTable.push(rowObject);
				self.sortTable[trSortRow].multiSort.push(rowObject);
			}
		}
	});
	return;
};

tiaacref.tableSort.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};
tiaacref.tableSort.prototype.getSortEventName = function () {
	"use strict";
	return tiaacref.defaults.tableSort.sortEventName + this.arg.id;
};
tiaacref.tableSort.prototype.getBeforeSortEventName = function () {
	"use strict";
	return tiaacref.defaults.tableSort.events.beforeSortName + this.getId();
};
tiaacref.tableSort.prototype.getAfterSortEventName = function () {
	"use strict";
	return tiaacref.defaults.tableSort.events.afterSortName + this.getId();
};
tiaacref.tableSort.prototype.reload = function() {
	"use strict";
	var jq = this.jq,
		arg = this.arg,
		sortHeaders,
		duplicateSortName = {},
		thSortName,
		self;
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'tableSort.reload');
	}
	sortHeaders = jq.id.find('thead:first tr th[data-sortname]');
	if (sortHeaders.length === 0) {
		throw tiaacref.error('', 'No sort columns (data-sortname=xxxx) could be found for arg.id in tableSort.');
	}
	sortHeaders.addClass('header').attr({'data-sortpath': 'none'});
	sortHeaders.unbind('click');
	this.jq = jq;
	this.sortHeaders = sortHeaders;
	this.sortTable = [];
	this.multiSortTable = [];
	this.sortParameters = [];
	self = this;
	sortHeaders.each(function (thIndex) {
		var thHeader = $(this),
			thSortName = thHeader.attr('data-sortname'),
			thObject = {
				index: thIndex,
				name: thSortName,
				dataType: thHeader.attr('data-sortdatatype'),
				format: thHeader.attr('data-sortformat')
			};
		self.sortParameters.push(thObject);
		if (thSortName != undefined) {
			if (typeof duplicateSortName[thSortName] !== 'undefined') {
				console.log("WARNING: Duplicate data-sortnames found (name = " + thSortName + ", table = " + self.arg.id + ")");
			}
			duplicateSortName[thSortName] = true;
		}
	});
	jq.id.find('tbody:first > tr').each(function (trIndex) {
		var trObject = $(this),
			rowObject = {
				id: trObject,
				index: trIndex
			};
		trObject.find('td').each(function (tdIndex) {
			var tdObject = $(this),
				attr = tdObject.attr('data-sortvalue');
			if (attr === undefined || attr === '') {
				rowObject[tdIndex] = tdObject.text();
			} else {
				rowObject[tdIndex] = tdObject.attr('data-sortvalue');
			}
		});
		if (!self.arg.multiSort) {
			self.sortTable.push(rowObject);
		} else {
			if (trObject.attr('data-sortmultisort') === 'true') {
				self.sortTable.push(rowObject);
			} else {
				self.multiSortTable.push(rowObject);
			}
		}
	});
	// assign clicks
	sortHeaders.click({self: this}, function (e) {
		var self = e.data.self,
			sortHeader = $(this),
			dataSortPath = sortHeader.attr('data-sortpath'),
			headerSortClass = 'header';
		if (dataSortPath === 'none') {
			dataSortPath = 'asc';
			headerSortClass = 'headerSortUp';
		} else if (dataSortPath === 'asc') {
			dataSortPath = 'desc';
			headerSortClass = 'headerSortDown';
		} else {
			dataSortPath = 'asc';
			headerSortClass = 'headerSortUp';
		}
		self.sortHeaders.removeClass('headerSortUp headerSortDown').addClass('header').attr({'data-sortpath': 'none'});
		sortHeader.addClass(headerSortClass).attr({'data-sortpath': dataSortPath});
		self.sort(sortHeader.attr('data-sortname'), dataSortPath);
		return false;
	});
	return;
};
tiaacref.tableSort.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('tableSort.' + eventName) + this.arg.id;
};












/* ==================================================== MAP ================================================ */

tiaacref.map = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.map,
		jq,
		googleString;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			type: defaults.type,
			size: defaults.size,
			transport: defaults.transport,
			address: defaults.address
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.type !== 'string') {
			arg.type = defaults.type;
		}
		if (typeof arg.size !== 'string') {
			arg.size = defaults.size;
		}
		if (typeof arg.address !== 'string') {
			arg.address = defaults.address;
		}
		if (typeof arg.transport !== 'string') {
			arg.transport = defaults.transport;
		}
	}
	if (typeof defaults.typeValues[arg.type] === 'undefined') {
		arg.type = defaults.type;
	}
	if (typeof defaults.sizeValues[arg.size] === 'undefined') {
		arg.size = defaults.size;
	}
	if (typeof defaults.transportValues[arg.transport] === 'undefined') {
		arg.transport = defaults.transport;
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'map');
	}
	this.arg = arg;
	this.jq = jq;
	jq.id.attr({
		alt: arg.address,
		title: arg.address
	});
	arg.address = arg.address.replace(/ /gi, '+');
	if (arg.type === 'static') {
		googleString =
			arg.transport + '://maps.googleapis.com/maps/api/staticmap' +
			'?center=' + arg.address +
			'&zoom=14' +
			'&size=' + defaults.sizeValues[arg.size].width + 'x' + defaults.sizeValues[arg.size].height +
			'&maptype=roadmap' +
			'&markers=color:green%7C' + arg.address +
			'&sensor=false';
//			'&sensor=false' +
//			'&client=<var class="apiparam">YOUR_CLIENT_ID</var>' +
//			'&signature=<var class="apiparam">SIGNATURE</var>'
		jq.id.attr({
			src: googleString
		});
	} else if (arg.type === 'dynamic') {
		// TODO add dynamic code for map
		googleString = 'dynamic value here';
	}
	return true;
};
tiaacref.map.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};










/* ================================================== Expand All ======================================= */
tiaacref.expandAll = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.expandAll,
		jq,
		varList,
		foundAllVariables,
		i,
		l,
		evalResult,
		titleParts;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			varList: defaults.varList,
			startCondition: defaults.startCondition,
			title: defaults.title
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.varList !== 'string') {
			arg.varList = defaults.varList;
		}
		if (typeof arg.startCondition !== 'string') {
			arg.startCondition = defaults.startCondition;
		}
		if (typeof arg.title !== 'string') {
			arg.title = defaults.title;
		}
	}
	if (typeof defaults.startConditionValues[arg.startCondition] === 'undefined') {
		arg.startCondition = defaults.startCondition;
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'expandAll');
	}
	if (arg.varList === '') {
		throw tiaacref.error('', 'arg.varList is empty in expandAll');
	}
	varList = arg.varList.split(',');
	foundAllVariables = true;
	for (i = 0, l = varList.length; i < l; i++) {
		evalResult = typeof window[varList[i]];
		if (evalResult === 'undefined') {
			foundAllVariables = false;
		} else if (!window[varList[i]] instanceof tiaacref.expandCollapse) {
			foundAllVariables = false;
		}
	}
	if (foundAllVariables === false) {
		throw tiaacref.error('', 'arg.varList contains at least one undefined variable in expandAll');
	}
	
	this.arg = arg;
	this.jq = jq;
	this.varList = varList;
	this.currentCondition = arg.startCondition;
	// process title
	this.title = {
		expanded: defaults.parameters.expanded.title,
		collapsed: defaults.parameters.collapsed.title
	};
	if (arg.title !== '') {
		titleParts = arg.title.split(";");
		if (titleParts.length === 1) {
			this.title.expanded = titleParts[0];
			this.title.collapsed = titleParts[0];
		} else {
			this.title.expanded = titleParts[1];
			this.title.collapsed = titleParts[0];
		}
	}
	this.doAction();
	jq.id.click({self: this}, function (e) {
		"use strict";
		e.data.self.toggle();
		return false;
	});
};
tiaacref.expandAll.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};
tiaacref.expandAll.prototype.getExpandEventName = function () {
	"use strict";
	return tiaacref.defaults.expandAll.expandEventName + this.getId();
};
tiaacref.expandAll.prototype.getCollapseEventName = function () {
	"use strict";
	return tiaacref.defaults.expandAll.collapseEventName + this.getId();
};
tiaacref.expandAll.prototype.getVariableList = function () {
	"use strict";
	return this.varList;
};
tiaacref.expandAll.prototype.expand = function() {
	"use strict";
	this.doAction("expanded");
	return;
};
tiaacref.expandAll.prototype.collapse = function() {
	"use strict";
	this.doAction("collapsed");
	return;
};
tiaacref.expandAll.prototype.toggle = function() {
	"use strict";
	if (this.currentCondition === "expanded") {
		this.collapse();
	} else {
		this.expand();
	}
	return;
};
//TODO: We cannot assume that the variable is in the global namespace!
tiaacref.expandAll.prototype.doAction = function(condition) {
	"use strict";
	var link = this.jq.id,
		i,
		l = this.varList.length,
		defaults = tiaacref.defaults.expandAll;
	if (typeof condition === "undefined") {
		condition = this.currentCondition;
	}
	link.removeClass(defaults.parameters[this.currentCondition].className);
	this.currentCondition = condition;
	link.addClass(defaults.parameters[this.currentCondition].className)
		.attr({title: this.title[this.currentCondition]})
		.html('<span class="icon"></span>' + this.title[this.currentCondition]);
	for (i = 0; i < l; i++) {
		if (this.currentCondition === "expanded") {
			window[this.varList[i]].expand();
		} else {
			window[this.varList[i]].collapse();
		}
	}
	tcoipc.fire(tiaacref.eventName("expandAll." + condition), this.getVariableList());
	return;
};
tiaacref.expandAll.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('expandAll.' + eventName) + this.arg.id;
};

















/* ============================================== ANALYTICS ======================================  */ 
tiaacref.analyticsDatabase = {};
tiaacref.analytics = function (arg) {
	"use defaults";
	var defaults = tiaacref.defaults.analytics;
	if (typeof arg !== 'object') {
		arg = {
			location: defaults.location,
			lazyLoad: defaults.lazyLoad,
			initialPageTrack: defaults.initialPageTrack
		};
	} else {
		if (typeof arg.location !== 'string') {
			arg.location = defaults.location;
		}
		if (typeof arg.lazyLoad !== 'boolean') {
			arg.lazyLoad = defaults.lazyLoad;
		}
		if (typeof arg.initialPageTrack !== 'boolean') {
			arg.initialPageTrack = defaults.initialPageTrack;
	}
	}
	this.arg = arg;
	this.attachedFunctions = [];
	return;
};

tiaacref.analytics.prototype.set = function (variableList, valueList, asIs) {
	"use strict";
	var defaults = tiaacref.defaults.analytics,
		database = tiaacref.analyticsDatabase,
		variables,
		values,
		newValue,
		asIs = (typeof asIs !== 'boolean' ? false : asIs),
		i,
		l;
	if (typeof variableList !== 'string') {
		throw tiaacref.error('badId', '', 'variableList argument is not a string');
	}
	if (typeof valueList !== 'string') {
		throw tiaacref.error('badId', '', 'variableList argument is not a string');
	}
	variables = tiaacref.parseCommaList(variableList);
	values = tiaacref.parseCommaList(valueList);
	if (asIs) {
		if (variables.length !== values.length) {
			if (variables.length > 1) {
				variables = [varaibles[1]];
			}
			values = [valueList];
		}
	} else {
	if (variables.length !== values.length) {
		throw tiaacref.error('', 'There must be an equal number of variables and values. # Variables = ' + variables.length + ', # Values = ' + values.length);
		}
	}
	l = variables.length;
	for (i = 0; i < l; i++) {
		if (asIs) {
			newValue = values[i];
		} else {
			newValue = values[i].replace(/@/g, '');
		}
		database[variables[i]] = newValue;			// use for obfuscated access
	}
	return;
};
tiaacref.analytics.prototype.get = function (variable) {
	"use strict";
	var defaults = tiaacref.defaults.analytics,
		database = tiaacref.analyticsDatabase,
		value,
		i,
		l;
	if (typeof variable !== 'string') {
		throw tiaacref.error('badId', '', 'variable argument is not a string');
	}

	if (variable.indexOf('s_omtr') > -1) {
		//fetch value from s_omtr
		value = s_omtr[variable.substring(7)];
	} else {
		value = database[variable];
	}
	return value;
};
tiaacref.analytics.prototype.attach = function (attachObject, attachFunction) {
	"use strict";
	if (typeof attachFunction === 'function') {
		this.attachedFunctions.push({attachObject: attachObject, attachFunction: attachFunction});
	}
	return;
};
tiaacref.analytics.prototype.track = function (initialPageTrack) {
	"use strict";
	var defaults = tiaacref.defaults.analytics,
		database = tiaacref.analyticsDatabase,
		self = this,
		i,
		l;
	if (tiaacref.useEnsighten) {
		return;
	}
	
	if (typeof initialPageTrack !== 'boolean') {
		initialPageTrack = this.getInitialPageTrack();
	}
	// if lazyload, we need to transfer any s_omtr tags set on the pages to the database.
	// IE7 fix
	if (s_omtr != null) {
		if (typeof s_omtr.t !== 'function') {
			for (i in s_omtr) {
				database[i] = s_omtr[i];
			}
			s_omtr = null;
		}
	}
	if (!this.arg.lazyLoad) {
		if (typeof s_omtr === 'undefined' || s_omtr === null) {
			throw tiaacref.error('', 'Analytics library not loaded, and lazyLoad is set to False.');
		}
		executeTrack(initialPageTrack);
	} else {
		$(window).load(function() {
			if (typeof s_omtr === 'undefined' || s_omtr === null) {
				$.ajax({
					type: 'GET',
					url: self.arg.location,
					data: null,
					success: function() {
						executeTrack(initialPageTrack);
						l = self.attachedFunctions.length;
						for (i = 0; i < l; i++) {
							self.attachedFunctions[i].attachFunction(self.attachedFunctions[i].attachObject);
						}
						tcoipc.fire('scodeLoad', {});
					},
					error: function() {
						throw tiaacref.error('', 'Analytics track error. Failed to load omniture script with AJAX.');
					},
					dataType: 'script',
					cache: true,
					crossDomain: true
				});
			} else {
				executeTrack(initialPageTrack);
			}
		});
	}

	function executeTrack(initialPageTrack) {
		if (typeof initialPageTrack !== 'boolean') {
			initialPageTrack = self.getInitialPageTrack();
		}
		if (s_omtr != null && initialPageTrack) {
			try {
				for (i in database) {
					s_omtr[i] = database[i];
				}
				var s_code = s_omtr.t();
				if(s_code){
					$('body').append(s_code);
				}
				self.setLazyLoad(false);
			} catch(err) {
				throw tiaacref.error('', 'Analytics track error.', err.message);
			}
		}
		self.setInitialPageTrack(true);
		self.setLazyLoad(false);
	}
};

tiaacref.analytics.prototype.trackLink = function (linkName, linkId, variableList, valueList, linkType) {
	"use strict";
	var defaults = tiaacref.defaults.analytics,
		elementId,
		database = tiaacref.analyticsDatabase,
		self,
		i;
	if (tiaacref.useEnsighten) {
		return;
	}
	if (typeof variableList === 'undefined') {
		variableList = '';
	}
	if (typeof valueList === 'undefined') {
		valueList = '';
	}
	if (typeof variableList !== 'string') {
		throw tiaacref.error('badId', '', 'variableList argument in trackLink, if specified, must be a string.');
	}
	if (typeof valueList !== 'string') {
		throw tiaacref.error('badId', '', 'valueList argument in trackLink, if specified, must be a string.');
	}
	if (typeof linkName !== 'string') {
		throw tiaacref.error('badId', '', 'linkName Custom Event Name in trackLink must be a string');
	}
	if (typeof linkId !== 'string') {
		throw tiaacref.error('badId', '', 'linkId in trackLink must be a string');
	}
	if (typeof linkType !== 'string') {
		linkType = defaults.linkType;
	}
	if (typeof defaults.validLinkType[linkType] === 'undefined') {
		linkType = defaults.linkType;
	}
	elementId = $(linkId);
	if (elementId.length === 0) {
		throw tiaacref.error('badId', '', 'linkId in trackLink is not a valid CSS selector.');
	}
	self = this;
	$(document).ready(function() {
		elementId.click(function (e) {
			self.set(variableList, valueList);
			for (i in database) {
				s_omtr[i] = database[i];
			}
			try {
				s_omtr.tl(this, linkType, linkName);
			} catch(err) {
				throw tiaacref.error('', 'Analytics trackLink error.', err.message);
			}
		});
	});
};
tiaacref.analytics.prototype.setProperties = function (arg) {
	"use strict";
	var variables,
		varList = '',
		valueList = '',
		i,
		l,
		tag,
		value;
	if (typeof arg !== 'string') {
		return;
	}
	if (arg === '') {
		return;
	}
	variables = arg.split(';');
	l = variables.length;
	for(i = 0; i < l; i++) {
		tag = variables[i].split(':');
		if (tag.length > 1) {
			if (varList !== '') {
				varList += ',';
			}
			varList += tag[0].trim();
			tag.shift();
			value = tag.join(':');
			if (valueList !== '') {
				valueList += ',';
			}
			valueList += value.trim();
		}
	}
	if (varList !== '' && valueList !== '') {
		this.set(varList, valueList);
	}
	return;
};
tiaacref.analytics.prototype.clear = function (variableList) {
	"use strict";
	var i,
		l;
	if (variableList){
		var variables = tiaacref.parseCommaList(variableList);
		var l = variables.length;
		for (i = 0; i < l; i++) {
			tiaacref.analyticsDatabase[variables[i]] = '';
			s_omtr[variables[i]] = '';
		}
	} else {
		tiaacref.analyticsDatabase = {};
		if (typeof s_omtr.clearVars === 'function') {
		s_omtr.clearVars();
	}
	}
	return;
};

tiaacref.analytics.prototype.setLocation = function (location) {
	"use strict";
	if (typeof location !== 'string') {
		throw tiaacref.error('badId', '', 'Argument location required or not a string in setLocation.');
	}
	this.arg.location = location;
	return;
};
tiaacref.analytics.prototype.getLocation = function () {
	"use strict";
	return this.arg.location;
};
tiaacref.analytics.prototype.setLazyLoad = function (lazyLoad) {
	"use strict";
	if (typeof lazyLoad !== 'boolean') {
		throw tiaacref.error('badId', '', 'Argument lazyLoad required or not a boolen in setLazyLoad.');
	}
	this.arg.lazyLoad = lazyLoad;
	return;
};
tiaacref.analytics.prototype.getLazyLoad = function () {
	"use strict";
	return this.arg.lazyLoad;
};
tiaacref.analytics.prototype.setInitialPageTrack = function (initialPageTrack) {
	"use strict";
	if (typeof initialPageTrack !== 'boolean') {
		throw tiaacref.error('badId', '', 'Argument initialPageTrack required or not a boolen in setInitialPageTrack (analytics).');
	}
	this.arg.initialPageTrack = initialPageTrack;
	return;
};
tiaacref.analytics.prototype.getInitialPageTrack = function () {
	"use strict";
	return this.arg.initialPageTrack;
};
tiaacref.analytics.prototype.toDataLayer = function (data) {
	var i;
	for (i in data) {
		tiaacref.adl.map(i, data[i]);
	}
};
tiaacref.analytics.prototype.addEvent = function (data) {
	return tiaacref.adl.map("event", data);
};
var tiaacrefAnalytics = new tiaacref.analytics();
if (typeof s_omtr === 'undefined') {
	 var s_omtr = {};
}
/* ------------------------------------------ PROGRESS BAR -------------------------------- */
tiaacref.progressBar = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.progressBar,
		jq;
	if (typeof arg === 'undefined') {
		arg = {
			id: defaults.id
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'progressBar');
	}
	this.jq = jq;
	return;
};
tiaacref.progressBar.prototype.getId = function() {
	"use strict";
	return this.arg.id;
};




/* ------------------------------------------ FLOATING CALLOUT ------------------------------ */
tiaacref.floatingCallout = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.floatingCallout,
		jq;
	if (typeof arg === 'undefined') {
		arg = {
			id: defaults.id
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'floatingCallout');
	}
	this.jq = jq;
	this.calloutHeight = jq.id.outerHeight(true);
	this.parent = jq.id.parent();
	this.condition = "top";
	this.oldWindowTop = 0;
	// must use inline styling because prototype uses !important, which destroys the normal operation of CSS, and prevents us 
	// from using classes.
	jq.id.css({position: 'relative', left: 0, top: 0, 'z-index': 99}).addClass('clearfix');
	$(window).scroll({self: this}, function(e) {
		var self = e.data.self,
			windowTop = $(window).scrollTop(),
			id = self.jq.id,
			firstParentTop = self.parent.offset().top,
			firstParentHeight = self.parent.outerHeight(),
			firstParentBottom = firstParentTop + firstParentHeight,
			parentTop = firstParentTop,
			newParent = self.parent,
			parentHeight = self.parent.outerHeight(),
			parentBottom = parentTop + parentHeight,
			idTop = id.offset().top,
			idHeight = id.height(),
			idBottom = idTop + idHeight,
			tagName = newParent.get(0).tagName;
		// find the parent that actually has height
		// or is the first larger than the floating callout
		while (parentHeight <= idHeight && tagName.toUpperCase() !== 'BODY') {
			newParent = newParent.parent();
			tagName = newParent.get(0).tagName;
			parentTop = newParent.offset().top;
			parentHeight = newParent.outerHeight();
			parentBottom = parentTop + parentHeight;
		}
//		console.log(windowTop + " (" + self.condition + ")" + ", id=(" + idTop + "," + idHeight + " = " + (idBottom) + "), parent=(" + parentTop + "," + parentHeight + " = " + (parentBottom) +"), firstParent=(" + firstParentTop + "," + firstParentHeight + " = " + (firstParentBottom) +")");
		if (windowTop > self.oldWindowTop) {
			if (self.condition !== "bottom") {
				if (windowTop + idHeight > parentBottom) {
					id.css({position: 'relative', left: 0, top: parentBottom - idHeight - firstParentTop});
					self.condition = "bottom";
				} else if (windowTop > idTop) {
					id.css({position: 'fixed', left: id.offset().left, top: 0});
					self.condition = "fixed";
				}
			}
		} else {
			if (self.condition !== "top") {
				if (windowTop < firstParentTop) {
					id.css({position: 'relative', left: 0, top: 0});
					self.condition = "top";
				} else if (windowTop + idHeight <= parentBottom) {
					id.css({position: 'fixed', left: id.offset().left, top: 0});
					self.condition = "fixed";
				}
			}
		}
		self.oldWindowTop = windowTop;
	});
	return;
};
tiaacref.floatingCallout.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};









/* ======================================================== LEGEND =================================================== */
tiaacref.legend = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.legend,
		jq;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style,
			cssClass: defaults.cssClass,
			data: defaults.data,
			format: defaults.format,
			columnBreak: defaults.columnBreak,
			showMore: defaults.showMore,
			expanded: defaults.expanded,
			showZeroPercentage: defaults.showZeroPercentage,
			suppressValueDisplay: defaults.suppressValueDisplay
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defatuls.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof arg.cssClass !== 'string') {
			arg.cssClass = defaults.cssClass;
		}
		if (typeof defaults.validStyle[arg.style] === 'undefined') {
			arg.style = defaults.style;
		}
		if (typeof arg.data !== 'object') {
			arg.data = defaults.data;
		}
		if (typeof arg.format !== 'string') {
			arg.format = defaults.format;
		}
		if (typeof arg.showZeroPercentage !== 'boolean') {
			arg.showZeroPercentage = defaults.showZeroPercentage;
		}
		if (typeof arg.suppressValueDisplay !== 'boolean') {
			arg.suppressValueDisplay = defaults.suppressValueDisplay;
		}
		if (!tiaacref.isNumber(arg.columnBreak)) {
			arg.columnBreak = defaults.columnBreak;
		} else {
			arg.columnBreak = parseInt(arg.columnBreak);
		}
		if (typeof arg.showMore !== 'string') {
			arg.showMore = defaults.showMore;
	}
		if (arg.showMore.indexOf("|") === -1 && arg.showMore !== "false") {
			arg.showMore = defaults.showMore;
		}
		if (typeof arg.expanded !== 'boolean') {
			arg.expanded = defaults.expanded;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'legend');
	}
	this.jq = jq;
	this.arg = arg;
	this.draw(arg.data);
	return;
};
tiaacref.legend.prototype.draw = function(data) {
	"use strict";
	var defaults = tiaacref.defaults.legend,
		arg = this.arg,
		jq = this.jq,
		style = defaults.validStyle[arg.style],
		format = arg.format,
		table,
		tbody,
		tr,
		td,
		span,
		showMore,
		a,
		i,
		l,
		pi,
		pl,
		text,
		color,
		total,
		value,
		columnBreak,
		columnNumber,
		dataCount,
		rowNumber,
		oldFormat,
		tooltip,
		newId,
		href,
		linkData,
		lastPushed,
		pointClasses,
		pointClass,
		newLabelText = [],
		rows = [];
	if (typeof data !== 'object') {
		throw tiaacref.error('', 'arg.data is not an object legend.draw');
	}
	this.arg.data = data;
	if (arg.style === 'custom') {
		if (!data instanceof tiaacref.data) {
			throw tiaacref.error('', 'arg.data is not an tiaacref.data in legend.draw');
		}
		showMore = arg.showMore.split("|");
		table = $('<table>').addClass(style.tableClass).addClass(arg.cssClass);
		if (format === '') {
			format = style.format;
		}
		data = data.seriesArray;
		l = data.length;
		for (i = 0; i < l; i++) {
			tbody = $('<tbody>');
			tr = $('<tr>').addClass('category');
			td = $('<td>').addClass('charticon');
			span = $('<span>');
			if (typeof data[i].arg.colorClass === 'string') {
				span.addClass(data[i].arg.colorClass);
			} else {
				span.addClass('other').css({'background-color': data[i].arg.color, color: data[i].arg.color});
			}
			td.append(span);
			tr.append(td);
			td = $('<td>').addClass('fw').html(data[i].arg.name);
			tr.append(td);
			td =  $('<td>').addClass('txtr prm');
			td.append($('<span>').addClass('guaranteedPercent').text(tiaacref.format(data[i].percentage, format)));
			tr.append(td);
			tbody.append(tr);
			// points
			pl = data[i].pointArray.length;
			for (pi = 0; pi < pl; pi ++) {
				tr = $('<tr>').addClass('hidden');
				td = $('<td>').html('&nbsp;');
				tr.append(td);
				td = $('<td>');
				if (typeof data[i].pointArray[pi].arg.linkData !== 'undefined') {
					linkData = data[i].pointArray[pi].arg.linkData;
					href = (typeof data[i].pointArray[pi].arg.linkData.href !== 'undefined' ? data[i].pointArray[pi].arg.linkData.href : '#');
					a = $('<a>')
						.addClass(typeof linkData.cssClass !== 'undefined' ? linkData.cssClass : '')
						.attr({href: href})
						.html(data[i].pointArray[pi].name + ' <span class="icon"></span>');
				} else {
					a = data[i].pointArray[pi].name;
				}
				td.append(a);
				tr.append(td);
				td = $('<td>').addClass('numeric').html(tiaacref.format(data[i].pointArray[pi].percentage, '%0.0'));
				tr.append(td);
				tbody.append(tr);
			}
			table.append(tbody);
		}
		jq.id.empty().append(table);
		if (arg.expanded) {
			expandShowMore(null, table, showMore);
		} else {
			collapseShowMore(null, table, showMore);
		}
		if (showMore.length >= 2) {
			newId = "showMore" + this.arg.id.replace('#', '');
			a = $('<a>')
				.addClass("moreFundsViewExpandCollapse")
				.attr({href: '#', id: 'showMore' + newId});
			if (arg.expanded) {
				expandShowMore(a, table, showMore);
			} else {
				collapseShowMore(a, table, showMore);
			}
			jq.id.append(
				$('<div>').addClass("txtb txts txtr asofdt mts")
				.append(a)
			);
			$('#showMore' + newId).on('click', function (event) {
				var a = $(this);
				event.stopPropagation();
				event.preventDefault();
				if (a.hasClass('addLink')) {
					expandShowMore(a, table, showMore);
				} else {
					collapseShowMore(a, table, showMore);
				}
			});
		}
		return;
	}
	if (data.length === 0) {
		return;
	}
	if (format === '') {
		format = style.format;
	}
	table = $('<table>');
	table.addClass(style.tableClass);
	table.addClass(arg.cssClass);
	tbody = $('<tbody>');
	l = data.length;
	columnBreak = (arg.columnBreak === 0 ? l : arg.columnBreak);
	for (i = 0; i < columnBreak; i++) {
		rows.push($('<tr>'));
	}
	dataCount = 0;
	columnNumber = 0;
	switch (arg.style) {
	case 'osda':
		tbody.addClass('alignt');
		for (i = 0; i < l ; i++) {
			if (data[i].tag !== 'undefined') {
				dataCount++;
				rowNumber = (dataCount - 1) % columnBreak;
				if (rowNumber === 0 && i !== 0) {
					columnNumber ++; 
				}
				if (columnNumber > 0) {
					rows[rowNumber].append($('<td>').addClass('plr nbsep nbdr').html('&nbsp;'));
				}
				if (format.indexOf('%') !== -1) {
					value = data[i].percentage;
					text = tiaacref.format(value, format);
				} else {
					value = data[i].value;
					text = tiaacref.format(value, format);
				}				
				if (arg.showZeroPercentage || (!arg.showZeroPercentage && value > 0)) {
					if (data[i].color === 'auto') {
						color = tiaacref.nextColor(data[i].tag);
					} else {
						color = data[i].color;
					}
					rows[rowNumber].append($('<td>').addClass('charticon nbdr')
						.append($('<span>').addClass('other').css({'background-color': color, color: color}))
					);
					if (arg.suppressValueDisplay) {
						text = '';
					}
					rows[rowNumber].append($('<td>').addClass('nbdr')
						.append($('<span>').text(text))
					);
					rows[rowNumber].append($('<td>').addClass('nbdr').append($('<span>').html(data[i].label)));
				}
			}
		}
		dataCount ++;
		rowNumber = (dataCount - 1) % columnBreak;
		while (rowNumber !== 0) {
			rows[rowNumber].append($('<td>').addClass('plr nbsep nbdr').html('&nbsp;'));
			rows[rowNumber].append($('<td>').addClass('plr nbsep nbdr').html('&nbsp;'));
			rows[rowNumber].append($('<td>').addClass('plr nbsep nbdr').html('&nbsp;'));
			rows[rowNumber].append($('<td>').addClass('plr nbsep nbdr').html('&nbsp;'));
			dataCount ++;
			rowNumber = (dataCount - 1) % columnBreak;
		}
		l = rows.length;
		for (i = 0; i < l; i++) {
			tbody.append(rows[i]);
		}
		break;
	case 'piepercent':
	case 'normal':
	case 'piepercentdecimal':
	case 'percentvalue':
	default:
		for (i = 0; i < l ; i++) {
			pointClasses = (typeof data[i].arg.pointClasses !== 'object' ? {} : data[i].arg.pointClasses);
			if (data[i].tag !== 'undefined') {
				dataCount++;
				rowNumber = (dataCount - 1) % columnBreak;
				if (rowNumber === 0 && i !== 0) {
					columnNumber ++; 
				}
				if (columnNumber > 0) {
					rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
				}
				oldFormat = format;
				if (arg.style === 'percentvalue') {
					format = '%' + format;
				}
				if (format.indexOf('%') !== -1) {
					value = data[i].percentage;
					text = tiaacref.format(value, format);
				} else {
					value = data[i].value;
					text = tiaacref.format(value, format);
				}				
				if (arg.showZeroPercentage || (!arg.showZeroPercentage && value > 0)) {
					if (data[i].color === 'auto') {
						color = tiaacref.nextColor(data[i].tag);
					} else {
						color = data[i].color;
					}
					pointClass = (typeof pointClasses.chartIcon !== 'string' ? "" : pointClasses.chartIcon);
					rows[rowNumber].append($('<td>').addClass('charticon ' + pointClass)
						.append($('<span>').addClass('other').css({'background-color': color, color: color}))
					);
					// tooltip fix
					pointClass = (typeof pointClasses.label !== 'string' ? "" : pointClasses.label);
					if (typeof data[i].arg.legendHover === 'string') {
						newLabelText.push(tiaacref.produceTooltipHtml(data[i].label, 'icon'));
						lastPushed = newLabelText.length - 1; 
						newLabelText[lastPushed].html = newLabelText[lastPushed].html.replace('@hover@', data[i].arg.legendHover);
						rows[rowNumber].append($('<td>').addClass(pointClass).html(data[i].label + newLabelText[lastPushed].html));
					} else {
						rows[rowNumber].append($('<td>').addClass(pointClass).html(data[i].label));
					}
					if (arg.suppressValueDisplay) {
						text = '';
					}
					pointClass = (typeof pointClasses.value !== 'string' ? "" : pointClasses.value);
					rows[rowNumber].append($('<td>').addClass('percentage ' + pointClass)
						.append($('<span>').text(text))
					);
				}
				if (arg.style === 'percentvalue') {
					pointClass = (typeof pointClasses.percentage !== 'string' ? "" : pointClasses.percentage);
					rows[rowNumber].append($('<td>').addClass('percentage ' + pointClass).append($('<span>').text(tiaacref.format(data[i].value, '$' + oldFormat))));
				}
			}
		}
		dataCount ++;
		rowNumber = (dataCount - 1) % columnBreak;
		while (rowNumber !== 0) {
			rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			if (arg.style === 'percentvalue') {
				rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			}
			dataCount ++;
			rowNumber = (dataCount - 1) % columnBreak;
		}
		l = rows.length;
		for (i = 0; i < l; i++) {
			tbody.append(rows[i]);
		}
		break;
	case 'piedollar':
	case 'pieLTD':
		total = 0;
		for (i = 0; i < l; i++) {
			pointClasses = (typeof data[i].arg.pointClasses !== 'object' ? {} : data[i].arg.pointClasses);
			if (data[i].tag !== 'undefined') {
				if (data[i].color === 'auto') {
					color = tiaacref.nextColor(data[i].tag);
				} else {
					color = data[i].color;
				}
			}
			dataCount++;
			rowNumber = (dataCount - 1) % columnBreak;
			if (rowNumber === 0 && i !== 0) {
				columnNumber ++; 
			}
			if (columnNumber > 0) {
				rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			}
			pointClass = (typeof pointClasses.chartIcon !== 'string' ? "" : pointClasses.chartIcon);
			rows[rowNumber].append($('<td>').addClass('charticon ' + pointClass)
				.append($('<span>').addClass('other').css({'background-color': color, color: color}))
			);
			// tooltip fix
			pointClass = (typeof pointClasses.label !== 'string' ? "" : pointClasses.label);
			if (typeof data[i].arg.legendHover === 'string') {
				newLabelText.push(tiaacref.produceTooltipHtml(data[i].label, 'icon'));
				lastPushed = newLabelText.length - 1; 
				newLabelText[lastPushed].html = newLabelText[lastPushed].html.replace('@hover@', data[i].arg.legendHover);
				rows[rowNumber].append($('<td>').addClass(pointClass).html(data[i].label + newLabelText[lastPushed].html));
			} else {
				rows[rowNumber].append($('<td>').addClass(pointClass).html(data[i].label));
			}
			text = tiaacref.format(data[i].value, format);
			total += data[i].value;
			pointClass = (typeof pointClasses.value !== 'string' ? "" : pointClasses.value);
			rows[rowNumber].append($('<td>').addClass('percentage ' + pointClass)
				.append($('<span>').text(text))
			);
		}
		dataCount ++;
		rowNumber = (dataCount - 1) % columnBreak;
		while (rowNumber !== 0) {
			rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			rows[rowNumber].append($('<td>').addClass('plr nbsep').html('&nbsp;'));
			dataCount ++;
			rowNumber = (dataCount - 1) % columnBreak;
		}
		l = rows.length;
		for (i = 0; i < l; i++) {
			tbody.append(rows[i]);
		}
		tr = $('<tr>');
		tr.addClass("nbdr");
		tr.append($('<td>'));
		tr.append($('<td>').append($('<strong>').text('Total')));
		if (arg.suppressValueDisplay) {
			text = '&nbsp;';
		} else  {
			text = tiaacref.format(total, format);
		}
		
		tr.append($('<td>').addClass('percentage')
			.append($('<strong>').text(text))
		);
		tbody.append(tr);
		break;
	}
	table.append(tbody);
	jq.id.empty().append(table);
	l = newLabelText.length;
	for (i = 0; i < l; i++) {
		new tiaacref.tooltip({tooltip: '#' + newLabelText[i].tooltipId, attachTo: '#' + newLabelText[i].id});
	}
	return;
	function expandShowMore(a, table, showMore) {
		if (a != null) {
			a.removeClass('addLink').addClass('minusLink').html('<span class="icon">-</span> ' + showMore[1]);
		}
		table.children('tbody').each(function (index) {
			$(this).children('tr:gt(0)').removeClass('hidden');
		});
	}
	
	function collapseShowMore(a, table, showMore) {
		if (a != null) {
			a.removeClass('minusLink').addClass('addLink').html('<span class="icon">+</span> ' + showMore[0]);
		}
		table.children('tbody').each(function (index) {
			$(this).children('tr:gt(0)').addClass('hidden');
		});
	}
};
tiaacref.legend.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('legend.' + eventName) + this.arg.id;
};










/* ======================================================== CHART =================================================== */
/*
 * All drawing routines currently use the Highcharts engine. Flot has been retired.
 */
tiaacref.chart = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.chart,
		jq,
		self,
		parent,
		sizes;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style,
			size: defaults.size,
			cssClass: defaults.cssClass,
			data: defaults.data,
			chartOptions: defaults.chartOptions,
			type: defaults.type
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defatuls.id;
		}
		if (typeof arg.cssClass !== 'string') {
			arg.cssClass = defaults.cssClass;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof defaults.validStyle[arg.style] === 'undefined') {
			arg.style = defaults.style;
		}
		if (typeof arg.size !== 'string') {
			arg.size = defaults.size;
		}
		if (typeof arg.chartOptions !== 'object') {
			arg.chartOptions = defaults.chartOptions;
		}
		if (typeof defaults.validSize[arg.size] === 'undefined') {
			if (arg.size.indexOf(',') !== -1) {
				sizes = arg.size.split(',');
				if (sizes.length === 2) {
					if (tiaacref.isNumber(sizes[0].trim()) && tiaacref.isNumber(sizes[1].trim())) {
						arg.customSize = {width: parseInt(sizes[0].trim()), height: parseInt(sizes[1].trim())};
						arg.size = 'custom';
					} else {
						arg.size = defaults.size;
					}
				} else {
					arg.size = defaults.size;
				}
			} else {
				if (tiaacref.isNumber(arg.size.trim())) {
					arg.customSize = {width: parseInt(arg.size.trim()), height: parseInt(arg.size.trim())};
					arg.size = 'custom';
				} else {
					arg.customSize = {width: 0, height: 0};
					arg.size = defaults.size;
				}
			}
		}
		if (typeof arg.type !== 'string') {
			arg.type = defaults.type;
		}
		if (typeof defaults.validType[arg.type] === 'undefined') {
			arg.type = defaults.type;
		}
		if (typeof arg.data !== 'object') {
			arg.data = defaults.data;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'chart');
	}
	this.jq = jq;
	this.arg = arg;
	// Tooltip constants
	this.TOOLTIP_FLOATTOP = 0;
	this.TOOLTIP_FLOATRIGHT = 1;
	this.TOOLTIP_FLOATBOTTOM = 2;
	this.TOOLTIP_FLOATLEFT = 3;
	this.TOOLTIP_GRAPHTOP = 4;
	this.TOOLTIP_GRAPHRIGHT = 5;
	this.TOOLTIP_GRAPHBOTTOM = 6;
	this.TOOLTIP_GRAPHLEFT = 7;
	this.TOOLTIP_POINTTOP = 8;
	this.TOOLTIP_POINTRIGHT = 9;
	this.TOOLTIP_POINTBOTTOM = 10;
	this.TOOLTIP_POINTLEFT = 11;
	self = this;
	this.chart = null;
	if (arg.cssClass !== '') {
		jq.id.addClass(arg.cssClass);
	}
	if (arg.size === 'auto') {
		parent = jq.id.parent();
		jq.id.css({width: parent.width() + 1, height: parent.height() + 1});
	} else if (arg.size === 'custom') {
		jq.id.css({width: arg.customSize.width, height: arg.customSize.height});
	} else {
		jq.id.css({width: defaults.validSize[arg.size].width, height: defaults.validSize[arg.size].height});
	}
	this.draw(arg.data);
	return;
};
tiaacref.chart.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('chart.' + eventName) + this.arg.id;
};


tiaacref.chart.prototype.draw = function(data) {
	"use strict";
	if (typeof data !== 'undefined') {
		if (typeof data !== 'object') {
			throw tiaacref.error('', 'argument data is not an object chart.draw()');
		}
		this.arg.data = data;
	} else {
		data = this.arg.data;
	}
	switch (this.arg.type) {
	case 'pie':
		this.drawPie(data);
		break;
	case 'bar':
		this.drawBar(data);
		break;
	case 'stackedBar':
		this.drawStackedBar(data);
		break;
	case 'area':
		this.drawArea(data);
		break;
	case 'stackedArea':
		this.drawStackedArea(data);
		break;
	case 'stockChart':
		this.drawStockChart(data);
		break;
	case 'line':
		this.drawLine(data);
		break;
	case 'custom':
		this.drawCustom(data);
		break;
	};
};


/* ----------------------------------- HIGHCHART MODIFICATIONS -------------------------------- */
tiaacref.chart.prototype.drawTooltip = function(obj, pos) {
	"use strict";
	var tooltip,
		pointer,
		hover,
		i,
		l,
		dataList,
		otherTooltip,
		chart,
		chartParent,
		tooltipParent,
		tcCustom,
		px,
		py,
		trackerOffset,
		removeClass,
		addClass,
		data,
		iX,
		iXl,
		shapeType;
	if (typeof pos !== 'number') {
		pos = this.TOOLTIP_GRAPHRIGHT;
	}
	if (typeof obj === 'undefined') {
		if (this.arg.data instanceof tiaacref.series) {
			dataList = this.arg.data.pointArray;
		} else if (this.arg.data instanceof Array) {
			dataList = this.arg.data;
		} else if (this.arg.data instanceof tiaacref.data) {
			dataList = [];
			l = this.arg.data.seriesArray.length;
			for (i = 0; i < l; i ++) {
				iXl = this.arg.data.seriesArray[i].pointArray.length;
				for (iX = 0; iX < iXl; iX ++) {
					dataList.push(this.arg.data.seriesArray[i].pointArray[iX]);
				}
			}
		} else {
			dataList = [];
		}
		l = dataList.length;
		for (i = 0; i < l; i++) {
			data = dataList[i];
			if (data instanceof tiaacref.series) {
				iXl = data.pointArray.length;
				for (iX = 0; iX < iXl; iX++) {
					otherTooltip = 	data.pointArray[iX].arg.tooltip;
					if (otherTooltip instanceof tiaacref.tooltip) {
						otherTooltip.jq.tooltip.addClass('hidden').css({display: 'none'})
						.attr({'aria-hidden': true});
					}
				}
			} else if (data instanceof tiaacref.point) {
				otherTooltip = 	data.arg.tooltip;
				if (otherTooltip instanceof tiaacref.tooltip) {
					otherTooltip.jq.tooltip.addClass('hidden').css({display: 'none'})
						.attr({'aria-hidden': true});
				}
			}
		}
	} else {
		tcCustom = obj.tcCustom;
		tcoipc.fire(
				tiaacref.eventName('chart.pointHover') + this.arg.id,
				tcCustom
			);
		tooltip = tcCustom.arg.tooltip;
		hover = tcCustom.arg.hover;
		chart = this.jq.id;
		chartParent = chart.parent();
		if (tooltip instanceof tiaacref.tooltip) {
			tooltipParent = tooltip.jq.tooltip.parent();
			if (typeof hover === 'string') {
				tooltip.replace(hover);
			}
			if (this.arg.data instanceof tiaacref.series) {
				dataList = this.arg.data.pointArray;
			} else if (this.arg.data instanceof Array) {
				dataList = this.arg.data;
			} else {
				dataList = [];
			}
			l = dataList.length;
			for (i = 0; i < l; i++) {
				data = dataList[i];
				if (data instanceof tiaacref.series) {
					iXl = data.pointArray.length;
					for (iX = 0; iX < iXl; iX++) {
						otherTooltip = 	data.pointArray[iX].arg.tooltip;
						if (otherTooltip instanceof tiaacref.tooltip) {
							otherTooltip.jq.tooltip.addClass('hidden').css({display: 'none'})
								.attr({'aria-hidden': true});
						}
					}
				} else if (data instanceof tiaacref.point) {
					otherTooltip = 	data.arg.tooltip;
					if (otherTooltip instanceof tiaacref.tooltip) {
						otherTooltip.jq.tooltip.addClass('hidden').css({display: 'none'})
							.attr({'aria-hidden': true});
					}
				}
			}
			// make sure the toolip is a sibling of the chart
			if (tooltipParent.attr('id') !== chartParent.attr('id')) {
//				chart.append(tooltip.jq.tooltip);
				chartParent.append(tooltip.jq.tooltip);
			}
			pointer = tooltip.jq.tooltip.find('.pointer');
			tooltip.jq.tooltip.css({display: 'block'}).removeClass('hidden')
				.attr({'aria-hidden': false});		// we do this so that we can get an accurate height
			if (typeof obj.tracker !== 'undefined') {
				trackerOffset = $(obj.tracker.element).position();
			} else if (typeof obj.graphic !== 'undefined') {
				trackerOffset = $(obj.graphic.element).position();
			}
			// next four lines are defaults (so they're not repeated constantly in the switch statement below
//			px = trackerOffset.left;
//			py = trackerOffset.top;	
			px = chart.position().left;
			py = chart.position().top;	
			removeClass = 'hidden pointerTop pointerRight pointerLeft';	
			addClass = 'pointerBottom';
			shapeType = (typeof obj.shapeType === 'undefined' ? 'area' : obj.shapeType);
			switch (pos) {
			case this.TOOLTIP_GRAPHRIGHT:
			default:
				px = chart.position().left + chart.outerWidth(false);
				py = chart.position().top + (chart.outerHeight(false) / 2) - (tooltip.jq.tooltip.outerHeight(false) / 2);
				removeClass = 'hidden pointerBottom pointerTop pointerRight';
				addClass = 'pointerLeft';
				break;
			case this.TOOLTIP_POINTRIGHT:
				removeClass = 'hidden pointerTop pointerRight pointerBottom';
				addClass = 'pointerLeft';
				switch (shapeType) {
				case 'rect':
					px = chart.position().left + obj.series.xAxis.left + obj.plotX + (obj.shapeArgs.width / 2);
					py = chart.position().top + obj.plotY;
					break;
				}
				break;
			case this.TOOLTIP_POINTTOP:
				removeClass = 'hidden pointerTop pointerRight pointerLeft';
				addClass = 'pointerBottom';
				switch (shapeType) {
				case 'rect':
					px = obj.barX;
					py = chart.position().top + obj.plotY - (obj.negative ? obj.shapeArgs.height : 0)
						- tooltip.jq.tooltip.outerHeight(true);
					break;
				case 'area':
					py = chart.position().top + obj.plotY - tooltip.jq.tooltip.outerHeight(true) - 5;
					px = chart.position().left + obj.series.xAxis.left + obj.plotX 
						- (tooltip.jq.tooltip.outerWidth(false) / 2) + (pointer.outerWidth(true) / 2);
					break;
				}
				
			}
			// ------------------ FIX TO OFF SCREEN TOOLTIP
			var pointerChange = {x: 0, y: 0};
			if (px < 4) {
				pointerChange.x = Math.abs(px - 4);
				px = Math.max(4, px);
			}
			// ------------------ END FIX
			tooltip.jq.tooltip
				.css({
					left: px, 
					top: py,
					display: 'block'
				})
				.removeClass(removeClass)
				.addClass(addClass)
				.attr({'aria-hidden': false});
			if (pos === this.TOOLTIP_POINTTOP) {
				pointer.css({left: tooltip.jq.tooltip.outerWidth() / 2 - pointerChange.x});
			} else {
				pointer.css({left: ''});
			}
		}
	}
	return;
};

tiaacref.chart.prototype.drawStockChart = function(data) {
	"use strict";
	var defaults = tiaacref.defaults.chart,
		arg = this.arg,
		jq = this.jq,
		style = defaults.validStyle[arg.style],
		pieData = [],
		self,
		i,
		l,
		value,
		strokeWidth = style.strokeWidth,
		chart,
		label,
		hcData = [],
		navigatorOptions,
		chartBorderWidth;
	if (typeof data !== 'object') {
		throw tiaacref.error('', 'arg.data is not an object chart.drawStockChart');
	}
	if (data instanceof Array) {
		hcData = data;
		label = 'Price';
	} else if (typeof data.data !== 'undefined') {
		if (!data.data instanceof Array) {
			throw tiaacref.error('', 'arg.data.data is not an array');
		}
		if (typeof data.label !== 'string') {
			data.label = 'Price';
		}
		hcData = data.data;
		label = data.label;
	} else {
		if (typeof data.x === 'undefined' || typeof data.y === 'undefined') {
			throw tiaacref.error('', 'arg.data.x or arg.data.y is not set in chart.drawStockChart');
		}
		if (data.x.length != data.y.length) {
			throw tiaacref.error('', 'arg.data.x and arg.data.y must have the same number of elements in chart.drawStockChart');
		}
		if (typeof data.label !== 'string') {
			data.label = 'Price';
		}
		label = data.label;
		this.arg.data = data;
		this.lastDataPoint = -1;
		if (data.x.length === 0) {
			return;
		}
		l = data.x.length;
		chartBorderWidth = (l == 1 ? 0 : 1);
		
		for (i = 0; i < l; i++) {
			hcData.push([data.x[i].value, data.y[i].value]);
		}
	}
	self = this;
	if (MYDATA !== {}) {
		navigatorOptions = {enabled: true, data: MYDATA};
	} else {
		navigatorOptions = {enabled: true};
	}
	this.chart = new Highcharts.StockChart({
		chart: {
			renderTo: jq.id[0]
		},
		credits: {
			enabled: false
		},
		rangeSelector: {
			selected: 1
		},
		title: {
			text: ''
		},
		navigator: navigatorOptions,
		xAxis: {
			events: {
				setExtremes: function (e) {
					tcoipc.fire(self.event('rangeChange'), {min: e.min, max: e.max});
				}
			}
		},
		series: [{
			name: label,
			data: hcData,
			tooltip: {
				valueDecimals: 2
			},
			point: {
				events: {
					mouseOver: function(e) {
						tcoipc.fire(self.event('pointHover'), {point: e.currentTarget});
					},
					click: function(e) {
						tcoipc.fire(self.event('pointSelect'), {point: e.point});
					}
				}
			}
		}]
	}, function(chart) {
		
	});
	return;
};


tiaacref.chart.prototype.drawPie = function(data) {
	"use strict";
	var defaults = tiaacref.defaults.chart,
		arg = this.arg,
		jq = this.jq,
		style = defaults.validStyle[arg.style],
		pieData = [],
		self,
		i,
		l,
		value,
		strokeWidth = style.strokeWidth,
		chart,
		hcData = [],
		chartBorderWidth;
	if (typeof data !== 'object') {
		throw tiaacref.error('', 'arg.data is not an object chart.drawPie');
	}
	this.arg.data = data;
	this.lastDataPoint = -1;
	if (data.length === 0) {
		return;
	}
	self = this;
	l = data.length;
	chartBorderWidth = (l == 1 ? 0 : 1);
	
	for (i = 0; i < l; i++) {
		if (data[i].percentage < style.minimumPercentage && data[i].percentage > 0) {
			value = (data[i].value / data[i].percentage) * style.minimumPercentage;
		} else {
			value = data[i].value;
		}
		hcData.push({
			name: data[i].label,
			color: (data[i].color == 'auto' ? tiaacref.nextColor(data[i].tag) : data[i].color), 
			y: value,
			tcCustom: {arg: data[i].arg}
		});
	}
	this.drawChart({
		type: 'pie',
		tooltipOptions: {
			enabled: false
		},
		chart: {
			spacingTop: 0,
			spacingRight: 0,
			spacingBottom: 0,
			spacingLeft: 0,
			margin: [0,0,0,0],
			allowPointSelect: false
		},
		plotOptions: {
			allowPointSelect: false,
			cursor: '',
			animation: false,
			shadow: false,
			size: '100%',
			borderWidth: chartBorderWidth,
			dataLabels: {
				enabled: false
			}
		},
		data: hcData
	});
	return;
};

tiaacref.chart.prototype.drawBar = function (data) {
	"use strict";
	var arg = this.arg,
		jq = this.jq,
		chartData = [],
		colorData = [],
		i,
		j,
		l,
		t1,
		opposite = (typeof arg.chartOptions.yAxisPosition === 'undefined' ? 'left' : arg.chartOptions.yAxisPosition),
		showYAxis = (typeof arg.chartOptions.showYAxis !== 'boolean' ? true : arg.chartOptions.showYAxis),
//		yAxisFormat = (typeof arg.chartOptions.yAxisFormat === 'undefined' ? '$0.2' : arg.chartOptions.yAxisFormat),
		yAxisFormat = data[0].arg.yAxisFormat,
		hcData = {
			xAxisLabels: [],
			series: []
		},
		self;
	if (typeof data !== 'object') {
		throw tiaacref.error('', 'arg.data is not an object chart.drawBar');
	}
	this.arg.data = data;
	this.lastDataPoint = -1;
	if (data.length === 0) {
		return;
	}
	l = data.length;
	for (i = 0; i < l; i++) {
		hcData.xAxisLabels.push(data[i].arg.xAxisLabel);
		hcData.series.push({
			color: data[i].color, 
			y: data[i].value,
			tcCustom: {arg: data[i].arg}
		});
	}
	
	this.drawChart({
		type: 'column',
		tooltipPosition: this.TOOLTIP_POINTTOP,
		tooltipOptions: {
			enabled: false
		},
		chart: {},
		xAxis: {
			categories: hcData.xAxisLabels,
			tickWidth: 0,
			lineColor: '#f1f1f1',
			labels: {
				style: {color: '#313131'}
			}
		},
		yAxis: {
			gridLineDashStyle: 'solid',
			gridLineColor: '#f1f1f1',
			minorGridLineDashStyle: 'solid',
			minorGridLineColor: '#f1f1f1',
			lineColor: '#f1f1f1',
			lineWidth: 1,
			opposite: opposite, 
			labels: {
				enabled: showYAxis,
				formatter: function() {
					return tiaacref.format(this.value, yAxisFormat);
				},
				style: {whiteSpace: 'nowrap', color: '#313131'}
			},
			title: {
				text: ''
			}
		},
		plotOptions: {
			animation: false,
			borderWidth: 0,
			shadow: false,
			groupPadding: .05
		},
		data: hcData.series
	});
	self = this;
	return;
	
};

tiaacref.chart.prototype.drawStackedBar = function(data) {
	"use strict";
	var arg = this.arg,
		jq = this.jq,
		chartData = [],
		colorData = [],
		i,
		l,
		series,
		seriesI,
		seriesL,
		opposite = (typeof arg.chartOptions.yAxisPosition === 'undefined' ? 'left' : arg.chartOptions.yAxisPosition),
		showYAxis = (typeof arg.chartOptions.showYAxis !== 'boolean' ? true : arg.chartOptions.showYAxis),
		yAxisFormat = (typeof arg.chartOptions.yAxisFormat === 'undefined' ? '$0.2' : arg.chartOptions.yAxisFormat),
		t1,
		pi,
		pl,
		hcData,
		self,
		color;
	if (typeof data !== 'object') {
		throw tiaacref.error('', 'arg.data is not an object chart.drawStackedBar');
	}
	this.arg.data = data;
	this.lastDataPoint = -1;
	if (data.length === 0) {
		return;
	}
	l = data.length;
	
	// Highcharts
	l = data.length;
	hcData = {
		xAxisLabels: [],
		series: []
	};
	for (i = 0; i < l; i++) {
		series = [];
		seriesL = data[i].pointArray.length;
		for (seriesI = 0; seriesI < seriesL; seriesI ++) {
			hcData.xAxisLabels.push(data[0].pointArray[seriesI].arg.xAxisLabel);
			series.push({
				color: data[i].pointArray[seriesI].color, 
				y: data[i].pointArray[seriesI].value,
				tcCustom: {arg: data[i].pointArray[seriesI].arg}
			});
		}
		hcData.series.push({data: series});
	}
	
	this.drawChart({
		type: 'column',
		tooltipPosition: this.TOOLTIP_POINTRIGHT,
		chart: {},
		tooltipOptions: {
			enabled: false
		},
		xAxis: {
			categories: hcData.xAxisLabels,
			tickWidth: 0,
			lineColor: '#f1f1f1',
			labels: {
				style: {color: '#313131'}
			}
		},
		yAxis: {
			gridLineDashStyle: 'solid',
			gridLineColor: '#f1f1f1',
			minorGridLineDashStyle: 'solid',
			minorGridLineColor: '#f1f1f1',
			lineColor: '#f1f1f1',
			lineWidth: 1,
			opposite: opposite,
			labels: {
				enabled: showYAxis,
				formatter: function() {
					return tiaacref.format(this.value, yAxisFormat);
				},
				style: {whiteSpace: 'nowrap', color: '#313131'}
			},
			title: {
				text: ''
			}
		},
		seriesOptions: {
			stacking: 'normal'
		},
		plotOptions: {
			animation: false,
			borderWidth: 0,
			shadow: false,
			groupPadding: .05
		},
		dataReplace: true,
		data: hcData.series
	});
	self = this;
	return;
};

tiaacref.chart.prototype.drawArea = function (data) {
	"use strict";
	var defaults = tiaacref.defaults.chart,
		arg = this.arg,
		jq = this.jq,
		style = defaults.validStyle[arg.style],
		chartData = [],
		tickData = [],
		color,
		self,
		i,
		l,
		value,
		tickFormatterData,
		opposite = (typeof arg.chartOptions.yAxisPosition === 'undefined' ? 'left' : arg.chartOptions.yAxisPosition),
		showYAxis = (typeof arg.chartOptions.showYAxis !== 'boolean' ? true : arg.chartOptions.showYAxis),
		yAxisFormat = (typeof arg.chartOptions.yAxisFormat === 'undefined' ? '$0.2' : arg.chartOptions.yAxisFormat),
		hcData = {
			xAxisLabels: [],
			series: []
		},
		actualData,
		areaChartColor,
		areaNegativeChartColor;
	if (typeof data !== 'object') {
		throw tiaacref.error('', 'arg.data is not an object chart.drawArea');
	}
	this.arg.data = data;
	if (data instanceof tiaacref.series) {
		actualData = data.pointArray;
		areaChartColor = data.arg.color;
		areaNegativeChartColor = data.arg.negativeColor;
	} else {
		actualData = data;
		if (actualData.length > 0) {
			areaChartColor = data[0].color;
			areaNegativeChartColor = data[0].negativeColor;
		}
	}
	this.lastDataPoint = -1;
	if (actualData.length === 0) {
		return;
	}
	self = this;
	l = actualData.length;
	for (i = 0; i < l; i++) {
		hcData.xAxisLabels.push(actualData[i].arg.xAxisLabel);
		hcData.series.push({
			color: actualData[i].color, 
			y: actualData[i].value,
			tcCustom: {arg: actualData[i].arg}
		});
	}
	this.drawChart({
		type: 'area',
		chart: {
			spacingBottom: 0,
			spacingLeft: 0,
			spacingRight: 0
		},
		xAxis: {
			categories: hcData.xAxisLabels,
			tickWidth: 0,
			lineColor: '#f1f1f1',
			labels: {
				style: {color: '#313131'}
			}
		},
		yAxis: {
			gridLineDashStyle: 'solid',
			gridLineColor: '#f1f1f1',
			minorGridLineDashStyle: 'solid',
			minorGridLineColor: '#f1f1f1',
			lineColor: '#f1f1f1',
			lineWidth: 1,
			opposite: opposite,
			labels: {
				enabled: showYAxis,
				formatter: function() {
					return tiaacref.format(this.value, yAxisFormat);
				},
				style: {whiteSpace: 'nowrap', color: '#313131'}
			},
			title: {
				text: ''
			}
		},
		tooltipPosition: this.TOOLTIP_POINTTOP,
		tooltipOptions: {
			enabled: false
		},
		plotOptions: {
			animation: false,
			borderWidth: 0,
			shadow: false,
			color: '#bbbb33',
			negativeFillColor: areaNegativeChartColor,
			negativeColor: '#bbbb33',
			fillColor: areaChartColor,
			marker: {
				enabled: true,
				symbol: 'circle',
				radius: 3,
				lineWidth: 2,
				states: {
					hover: {
						lineWidth: 2,
						enabled: true,
						radius: 6,
						fillColor: '#eeee88'
					}
				}
			}
		},
		data: hcData.series
	});
	self = this;
	return;
};

tiaacref.chart.prototype.drawStackedArea = function (data) {
	"use strict";
	var defaults = tiaacref.defaults.chart,
		arg = this.arg,
		jq = this.jq,
		style = defaults.validStyle[arg.style],
		chartData = [],
		tickData = [],
		color,
		self,
		i,
		l,
		value,
		opposite = (typeof arg.chartOptions.yAxisPosition === 'undefined' ? 'left' : arg.chartOptions.yAxisPosition),
		showYAxis = (typeof arg.chartOptions.showYAxis !== 'boolean' ? true : arg.chartOptions.showYAxis),
		yAxisFormat = (typeof arg.chartOptions.yAxisFormat === 'undefined' ? '%0.0' : arg.chartOptions.yAxisFormat),
		tickFormatterData,
		hcData = {
			xAxisLabels: [],
			series: []
		},
		seriesI,
		seriesL,
		series;
	if (typeof data !== 'object') {
		throw tiaacref.error('', 'arg.data is not an object chart.drawArea');
	}
	this.arg.data = data;
	this.lastDataPoint = -1;
	if (data.length === 0) {
		return;
	}
	self = this;

	l = data.length;
	hcData = {
		xAxisLabels: [],
		series: []
	};
	for (i = 0; i < l; i++) {
		series = [];
		seriesL = data[i].pointArray.length;
		for (seriesI = 0; seriesI < seriesL; seriesI ++) {
			hcData.xAxisLabels.push(data[0].pointArray[seriesI].arg.xAxisLabel);
			series.push({
				color: data[i].pointArray[seriesI].color, 
				y: data[i].pointArray[seriesI].value,
				tcCustom: {arg: data[i].pointArray[seriesI].arg}
			});
		}
		hcData.series.push({color: data[i].arg.color, data: series});
	}
	this.drawChart({
		type: 'area',
		chart: {
			spacingBottom: 0,
			spacingLeft: 0,
			spacingRight: 0
		},
		xAxis: {
			categories: hcData.xAxisLabels,
			tickWidth: 0,
			lineColor: '#f1f1f1',
			startOnTick: true,
			endOnTick: true,
			labels: {
				style: {color: '#313131'}
			}
		},
		yAxis: {
			gridLineDashStyle: 'solid',
			gridLineColor: '#f1f1f1',
			minorGridLineDashStyle: 'solid',
			minorGridLineColor: '#f1f1f1',
			lineColor: '#f1f1f1',
			lineWidth: 1,
			opposite: opposite,
			labels: {
				enabled: showYAxis,
				formatter: function() {
					return tiaacref.format(this.value / 100, yAxisFormat);
				},
				style: {whiteSpace: 'nowrap', color: '#313131'}
			},
			title: {
				text: ''
			}
		},
		tooltipPosition: this.TOOLTIP_POINTTOP,
		tooltipOptions: {
			enabled: false
		},
		seriesOptions: {
			stacking: 'percent'
		},
		plotOptions: {
			animation: false,
			borderWidth: 0,
			shadow: false,
			color: '#bbbb33',
			pointPadding: 0,
			marker: {
				enabled: true,
				symbol: 'circle',
				radius: 3,
				lineWidth: 2,
				states: {
					hover: {
						lineWidth: 2,
						enabled: true,
						radius: 6,
						fillColor: '#eeee88'
					}
				}
			}
		},
		dataReplace: true,
		data: hcData.series
	});
	self = this;
	return;
};

tiaacref.chart.prototype.drawLine = function (data) {
	var defaults = tiaacref.defaults.chart,
		arg = this.arg,
		jq = this.jq,
		style = defaults.validStyle[arg.style],
		chartData = [],
		tickData = [],
		color,
		self,
		i,
		l,
		value,
		fullData = [],
		fullDataIndex,
		fullDataLength,
		dataPoints,
		opposite = (typeof arg.chartOptions.yAxisPosition === 'undefined' ? 'left' : arg.chartOptions.yAxisPosition),
		showYAxis = (typeof arg.chartOptions.showYAxis !== 'boolean' ? true : arg.chartOptions.showYAxis),
		crosshairs = (typeof arg.chartOptions.tooltipCrosshairs !== 'undefined' ? arg.chartOptions.tooltipCrosshairs : false),
		yAxisFormat = (typeof arg.chartOptions.yAxisFormat === 'undefined' ? '$0.2' : arg.chartOptions.yAxisFormat),
		options = {
			tooltip: {
				enabled: (typeof arg.chartOptions.tooltipEnabled !== 'boolean' ? true : arg.chartOptions.tooltipEnabled),
				symbol: (typeof arg.chartOptions.tooltipSymbol !== 'string' ? 'circle' : arg.chartOptions.tooltipSymbol)
			},
			marker: {
				enabled: (typeof arg.chartOptions.markerEnabled !== 'boolean' ? true : arg.chartOptions.markerEnabled),
				symbol: (typeof arg.chartOptions.markerSymbol !== 'string' ? 'circle' : arg.chartOptions.markerSymbol)
			}
		},
		tickFormatterData,
		hcData = {
			xAxisLabels: [],
			series: []
		},
		actualData,
		chartColor;
	if (typeof data !== 'object') {
		throw tiaacref.error('', 'arg.data is not an object chart.drawLine');
	}
	this.arg.data = data;
	fullData = [];
	if (data instanceof tiaacref.series) {
		actualData = data.pointArray;
		chartColor = data.arg.color;
		fullData.push({data: actualData, color: chartColor, hoverSymbol: (typeof data.arg.hoverSymbol === 'undefined' ? options.tooltip.symbol : data.arg.hoverSymbol)});
	} else if (data instanceof Array) {
		l = data.length;
		for (i = 0; i < l; i ++) {
			fullData.push({data: data[i].pointArray, color: data[i].arg.color, hoverSymbol: (typeof data[i].arg.hoverSymbol === 'undefined' ? options.tooltip.symbol : data[i].arg.hoverSymbol)});
		};
	} else {
		actualData = data;
		if (actualData.length > 0) {
			chartColor = data[0].color;
		}
		fullData.push({data: actualData, color: chartColor, hoverSymbol: options.tooltip.symbol});
	}
	this.lastDataPoint = -1;
	dataPoints = 0;
	fullDataLength = fullData.length;
	for (fullDataIndex = 0; fullDataIndex < fullDataLength; fullDataIndex ++) {
		dataPoints = Math.max(fullData[fullDataIndex].data.length, dataPoints);
	}
	if (dataPoints === 0) {
		return;
	}
	self = this;
	chartData = [];
	for (fullDataIndex = 0; fullDataIndex < fullDataLength; fullDataIndex ++) {
		actualData = fullData[fullDataIndex].data;
	l = actualData.length;
		hcData = {xAxisLabels: [], series: []};
	for (i = 0; i < l; i++) {
		hcData.xAxisLabels.push(actualData[i].arg.xAxisLabel);
		hcData.series.push({
			color: actualData[i].color, 
				marker: {enabled: options.marker.enabled, symbol: fullData[fullDataIndex].hoverSymbol},
			y: actualData[i].value,
			tcCustom: {arg: actualData[i].arg}
		});
	}
		chartData.push({data: hcData.series, color: fullData[fullDataIndex].color});
	}
	opposite = (opposite === 'right' ? true : false);
	this.drawChart({
		type: 'line',
		chart: {},
		xAxis: {
			categories: hcData.xAxisLabels,
			tickWidth: 0,
			lineColor: '#f1f1f1',
			labels: {
				style: {color: '#313131'}
			}
		},
		yAxis: {
			gridLineDashStyle: 'solid',
			gridLineColor: '#f1f1f1',
			minorGridLineDashStyle: 'solid',
			minorGridLineColor: '#f1f1f1',
			lineColor: '#f1f1f1',
			lineWidth: 1,
			opposite: opposite,
			labels: {
				enabled: showYAxis,
				formatter: function() {
					return tiaacref.format(this.value, yAxisFormat);
				},
				style: {whiteSpace: 'nowrap', color: '#313131'}
			},
			title: {
				text: ''
			}
		},
		tooltipPosition: this.TOOLTIP_POINTTOP,
		tooltipOptions: {
			enabled: false,
			crosshairs : crosshairs
		},
		plotOptions: {
			animation: false,
//			borderWidth: 0,
			shadow: false,
			color: chartColor,
			marker: {
				enabled: options.marker.enabled,
				symbol: options.marker.symbol,
				radius: 2,
				lineWidth: 2,
				states: {
					hover: {
						lineWidth: 2,
						enabled: options.tooltip.enabled,
						symbol: options.tooltip.symbol,
						radius: 6,
						fillColor: chartColor
					}
				}
			}
		},
//		data: hcData.series
		data: chartData
	});
	self = this;
	return;
	
};



tiaacref.chart.prototype.drawCustom = function (data) {
	var defaults = tiaacref.defaults.chart,
		arg = this.arg,
		jq = this.jq,
		style = defaults.validStyle[arg.style],
		chartData = [],
		tickData = [],
		color,
		self,
		i,
		l,
		value,
		fullData = [],
		fullDataIndex,
		fullDataLength,
		dataPoints,
		opposite = (typeof arg.chartOptions.yAxisPosition === 'undefined' ? 'left' : arg.chartOptions.yAxisPosition),
		showYAxis = (typeof arg.chartOptions.showYAxis !== 'boolean' ? true : arg.chartOptions.showYAxis),
		yAxisFormat = (typeof arg.chartOptions.yAxisFormat === 'undefined' ? '$0.2' : arg.chartOptions.yAxisFormat),
		options = {
			tooltip: {
				enabled: (typeof arg.chartOptions.tooltipEnabled !== 'boolean' ? true : arg.chartOptions.tooltipEnabled),
				symbol: (typeof arg.chartOptions.tooltipSymbol !== 'string' ? 'circle' : arg.chartOptions.tooltipSymbol)
			},
			marker: {
				enabled: (typeof arg.chartOptions.markerEnabled !== 'boolean' ? true : arg.chartOptions.markerEnabled),
				symbol: (typeof arg.chartOptions.markerSymbol !== 'string' ? 'circle' : arg.chartOptions.markerSymbol)
			}
		},
		tickFormatterData,
		hcData = {
			xAxisLabels: [],
			series: []
		},
		actualData,
		chartType,
		markerEnabled,
		xAxis,
		xAxisData = {
			type: 'linear'
		},
		chartColor,
		pushData;
	if (typeof data !== 'object') {
		throw tiaacref.error('', 'arg.data is not an object chart.drawCustom');
	}
	this.arg.data = data;
	fullData = [];
	if (data instanceof tiaacref.data) {
		opposite = (typeof data.arg.yAxisPosition === 'undefined' ? opposite : data.arg.yAxisPosition);
		showYAxis = (typeof data.arg.showYAxis !== 'boolean' ? showYAxis : data.arg.showYAxis);
		xAxisData = (typeof data.arg.xAxis !== 'undefined' ? data.arg.xAxis: xAxisData);
		data = data.seriesArray;
		l = data.length;
		for (i = 0; i < l; i ++) {
			fullData.push({data: data[i].pointArray, fillColor: data[i].arg.fillColor, markerEnabled: data[i].arg.markerEnabled, type: data[i].arg.chartType, color: data[i].arg.color, hoverSymbol: (typeof data[i].arg.hoverSymbol === 'undefined' ? options.tooltip.symbol : data[i].arg.hoverSymbol)});
		};
	} else {
		throw tiaacref.error('', 'arg.data is not a tiaacref.data object in chart.drawCustom');
	}
	this.lastDataPoint = -1;
	dataPoints = 0;
	fullDataLength = fullData.length;
	for (fullDataIndex = 0; fullDataIndex < fullDataLength; fullDataIndex ++) {
		dataPoints = Math.max(fullData[fullDataIndex].data.length, dataPoints);
	}
	if (dataPoints === 0) {
		return;
	}
	self = this;
	chartData = [];
	for (fullDataIndex = 0; fullDataIndex < fullDataLength; fullDataIndex ++) {
		actualData = fullData[fullDataIndex].data;
		l = actualData.length;
		hcData = {xAxisLabels: [], series: []};
		for (i = 0; i < l; i++) {
			hcData.xAxisLabels.push(actualData[i].arg.xAxisLabel);
			hcData.series.push({
				color: actualData[i].color, 
				marker: {enabled: fullData[fullDataIndex].markerEnabled, symbol: fullData[fullDataIndex].hoverSymbol},
				y: actualData[i].value,
				tcCustom: {arg: actualData[i].arg}
			});
		}
//		chartData.push({data: hcData.series, xAxis: hcData.xAxisLabels});
		pushData = {data: hcData.series, color: fullData[fullDataIndex].color, type: fullData[fullDataIndex].type};
		if (typeof xAxisData.pointStart !== 'undefined') {
			pushData.pointStart = xAxisData.pointStart;
		}
		if (typeof xAxisData.pointInterval !== 'undefined') {
			pushData.pointInterval = xAxisData.pointInterval;
		}
		if (typeof fullData[fullDataIndex].fillColor !== 'undefined') {
			pushData.fillColor = fullData[fullDataIndex].fillColor;
		}
		chartData.push(pushData);
	}
	if (xAxisData.type === 'linear') {
		xAxis = {
			type: 'linear',
			categories: hcData.xAxisLabels,
			tickWidth: 0,
			lineColor: '#f1f1f1',
			labels: {
				style: {color: '#313131'}
		}
		};
		
	} else if (xAxisData.type === 'datetime') {
		xAxis = {
			type: 'datetime',
			// tickPixelInterval: 60,
			dateTimeLabelFormats: {
				month: '%b \'%y',
				day: '%m/%d/%y'
		}
		};
	}
	opposite = (opposite === 'right' ? true : false);
	this.drawChart({
		type: 'custom',
		chart: {},
		xAxis: xAxis,
		yAxis: {
			gridLineDashStyle: 'solid',
			gridLineColor: '#f1f1f1',
			minorGridLineDashStyle: 'solid',
			minorGridLineColor: '#f1f1f1',
			lineColor: '#f1f1f1',
			lineWidth: 1,
			opposite: opposite,
			labels: {
				enabled: showYAxis,
				formatter: function() {
					return tiaacref.format(this.value, yAxisFormat);
				},
				style: {whiteSpace: 'nowrap', color: '#313131'}
			},
			title: {
				text: ''
			}
		},
		tooltipPosition: this.TOOLTIP_POINTTOP,
		tooltipOptions: {
			enabled: false
		},
		plotOptions: {
			animation: false,
			shadow: false,
			color: chartColor
		},
		data: chartData
	});
	self = this;
	return;
	
};


tiaacref.chart.prototype.drawChart = function (chartOptions) {
	"use strict";
	var self = this,
		chartData = (chartOptions.type === 'line' || chartOptions.type === 'custom' ? chartOptions.data : [{data: chartOptions.data}]),
		chart = {
			chart: {
				renderTo: this.jq.id[0],
				backgroundColor: null,
				plotBackgroundColor: null,
				plotBorderWidth: null,
				plotShadow: false,
				type: (chartOptions.type === 'custom' ? '' : chartOptions.type)
			},
			credits: {
				enabled: false
			},
			title: {
				text: ''
			},
			subtitle: {
				text: ''
			},
			legend: {
				enabled: false
			},
			tooltip: chartOptions.tooltipOptions,
			xAxis: typeof chartOptions.xAxis !== 'undefined' ? chartOptions.xAxis : {},
			yAxis: typeof chartOptions.yAxis !== 'undefined' ? chartOptions.yAxis : {},
			plotOptions: {
				series: {
					point: {
						events: {
							mouseOver: function() {
								self.drawTooltip(this, 
									(typeof chartOptions.tooltipPosition !== 'number' 
										? self.TOOLTIP_GRAPHRIGHT 
										: chartOptions.tooltipPosition));
							}
						}
					},
					events: {
						mouseOut: function() {
							self.drawTooltip();
						}
					}
				}
			},
//			series: [{data: chartOptions.data}]
			series: chartData
		};
	chart.plotOptions[chartOptions.type] = chartOptions.plotOptions;
	chartOptions.dataReplace = (typeof chartOptions.dataReplace !== 'boolean' ? false: true);
	if (chartOptions.dataReplace) {
		chart.series = chartOptions.data;
	}
	$.extend(chart.chart, chartOptions.chart);
	$.extend(chart.plotOptions.series, (typeof chartOptions.seriesOptions !== 'object' ? {} : chartOptions.seriesOptions));
	this.chart = new Highcharts.Chart(chart);
};


/* ====================================================== DATA ============================================ */

tiaacref.data = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.data;
	if (typeof arg !== 'object') {
		arg = {
			id: tiaacref.createId(),
			display: defaults.display,
			exclude: defaults.exclude,
			format: defaults.format
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = tiaacref.createId();
		}
		if (typeof arg.display !== 'boolean') {
			arg.display = defaults.display;
		}
		if (typeof arg.exclude !== 'boolean') {
			arg.exclude = defaults.exclude;
		}
		if (typeof arg.format !== 'string') {
			arg.format = defaults.format;
		}
	}
	this.arg = arg;
	this.format = this.arg.format;
	this.series = {};
	this.total = 0;
	this.percentage = 100;
	this.seriesIncluded = 0;
	this.seriesCount = 0;
	this.seriesList = [];
	this.seriesArray = [];
};
tiaacref.data.prototype.getFirstSeries = function() {
	"use strict";
	if (this.seriesCount === 0) {
		return null;
	}
	return this.series[this.seriesList[0]];
};
tiaacref.data.prototype.addSeries = function(arg) {
	"use strict";
	var seriesId;
	if (typeof arg !== 'object') {
		throw tiaacref.error('', 'Bad or missing series arg in addSeries');
	}
	if (!(arg instanceof tiaacref.series)) {
		throw tiaacref.error('', 'Bad or missing series arg in addSeries. Not type of tiaacref.series');
	}
	if (typeof this.series[arg.id] !== 'undefined') {
		throw tiaacref.error('', 'Duplicate series ID');
	}
	seriesId = arg.arg.id;
	this.seriesArray.push(arg);
	this.series[seriesId] = arg;
	this.series[seriesId].tcdata = this;
	this.seriesCount++;
	this.seriesList.push(seriesId);
	this.recalculate();
	return true;
};
tiaacref.data.prototype.recalculate = function() {
	"use strict";
	var s,
		pct = 0;
	this.total=0;
	this.percentage=100;
	this.seriesIncluded=0;
	for(s in this.series) {
		if (!this.series[s].exclude) {
			this.total += this.series[s].total;
			this.seriesIncluded += this.series[s].pointsIncluded;
		}
	}
	if (this.total === 0) {
		pct = null;
	}
	for(s in this.series) {
		if (!this.series[s].exclude) {
			if (pct === null) {
				this.series[s].percentage = null;
			} else {
				this.series[s].percentage = this.series[s].total / this.total;
			}
		} else {
			this.series[s].percentage = null;
		}
	}
	return true;
};
tiaacref.data.prototype.excludeSeries =function(series, excludeFlag) {
	"use strict";
	var s = null;
	excludeFlag = (typeof excludeFlag !== 'boolean' ? true: false);
	if (typeof series === 'string') {
		if (typeof this.series[series] !== 'undefined') {
			s = this.series[series];
		}
	} else if (series instanceof tiaacref.series) {
		if (typeof this.series[series.id] !== 'undefined') {
			s = this.series[series.id];
		}
	}
	if (s === null) {
		throw tiaacref.error('', 'Series Name Missing');
	}
	s.exclude = excludeFlag;
	this.recalculate();
	return true;
};
tiaacref.data.prototype.filterSeries = function(series, filterFlag) {
	return this.excludeSeries(series, filterFlag);
};
tiaacref.data.prototype.getAllSeries = function() {
	return this.series;
};






/* ========================================= SERIES ==================================== */
tiaacref.series = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.series;
	if (typeof arg !== 'object') {
		arg = {
			id: tiaacref.createId(),
			label: defaults.label,
			display: defaults.display,
			exclude: defaults.exclude,
			color: defaults.color,
			negativeColor: defaults.negativeColor,
			format: defaults.format
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = tiaacref.createId();
		}
		if (typeof arg.label !== 'string') {
			arg.label = defaults.label;
		}
		if (typeof arg.display !== 'boolean') {
			arg.display = defaults.display;
		}
		if (typeof arg.exclude !== 'boolean') {
			arg.exclude = defaults.exclude;
		}
		if (typeof arg.color !== 'string') {
			arg.color = tiaacref.nextColor(arg.id);
		}
		if (typeof arg.negativeColor !== 'string') {
			arg.negativeColor = tiaacref.nextColor(arg.id);
		}
		if (arg.color === '') {
			arg.color = defaults.color;
		}
		if (typeof arg.format !== 'string') {
			arg.format = defaults.format;
		}
	}
	if (arg.color === '') {
		arg.color = tiaacref.nextColor(arg.id);
	}
	this.arg = arg;
	this.format = this.arg.format;
	this.points = {};
	this.pointCount = 0;
	this.pointList = [];
	this.pointArray = [];
	this.total = 0;
	this.percentage = 100;
	this.pointsIncluded = 0;
	this.tcdata = null;
};
tiaacref.series.prototype.addPoint = function(arg) {
	"use strict";
	var pointId;
	if (typeof arg !== 'object') {
		throw tiaacref.error('', 'Bad arg in addPoint. Should be object');
	}
	if (!(arg instanceof tiaacref.point)) {
		throw tiaacref.error('', 'Bad arg in addPoint. Should be of type tiaacref.point');
	}
	if (typeof this.points[arg.arg.id] !== 'undefined') {
		throw tiaacref.error('', 'Bad arg in addPoint. This ID is a duplicate ID');
	}
	pointId = arg.arg.id;
	this.pointArray.push(arg);
	this.pointList.push(pointId);
	this.points[pointId] = arg;
	this.points[pointId].series = this;
	this.pointCount++;
	this.recalculate();
	return true;
};

tiaacref.series.prototype.recalculate = function(){
	"use strict";
	var p,
		pct = 0;
	this.total = 0;
	this.percentage = 100;
	this.pointsIncluded = 0;
	for(p in this.points) {
		if (!this.points[p].exclude) {
			if (tiaacref.isNumber(this.points[p].value)) {
				this.total += this.points[p].value;
				this.pointsIncluded++;
			}
		}
	}
	pct = 0;
	if (this.total === 0) {
		pct = null;
	}
	for(p in this.points) {
		if (!this.points[p].exclude) {
			if (tiaacref.isNumber(this.points[p].value)) {
				if (pct == null) {
					this.points[p].percentage = null;
				} else {
					this.points[p].percentage = this.points[p].value / this.total;
				}
			}
		} else {
			this.points[p].percentage = null;
		}
	}
	if (this.tcdata instanceof tiaacref.data) {
		this.tcdata.recalculate();
	}
	return true;
};
tiaacref.series.prototype.excludePoint = function(point, excludeFlag) {
	"use strict";
	var p;
	excludeFlag = (typeof excludeFlag !== 'boolean' ? true: false);
	p = this.findPoint(point);
	if (p === null) {
		throw tiaacref.error('', 'point name is not a part of this series');
	}
	p.exclude = excludeFlag;
	this.recalculate();
	return true;
};
tiaacref.series.prototype.filterPoint = function(point, filterFlag) {
	"use strict";
	return this.excludePoint(point, filterFlag);
};
tiaacref.series.prototype.removePoint = function(point) {
	"use strict";
	var p = this.findPoint(point);
	if (p === null) {
		throw tiaacref.error('', 'point name is not a part of this series');
	}
	delete this.points[point.id];
	this.recalculate();
	return true;
};
tiaacref.series.prototype.findPoint=function(point) {
	"use strict";
	var p = null;
	if (typeof point === 'string') {
		if (typeof this.points[point] !== 'undefined') {
			p = this.points[point];
		}
	} else if (point instanceof tiaacref.point) {
		if (typeof this.points[point.id] !== 'undefined') {
			p = this.points[point.id];
		}
	}
	return p;
};




/* ========================================================= POINT =========================================== */
tiaacref.point = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.point;
	if (typeof arg !== 'object') {
		arg = {
			id: tiaacref.createId(),
			label: defaults.label,
			color: defaults.color,
			negativeColor: defaults.negativeColor,
			display: defaults.display,
			exclude: defaults.exclude,
			onClick: defaults.onClick,
			tooltip: defaults.tooltip,
			format: defatuls.format
		};
	}
	else {
		if (typeof arg.id !== 'string') {
			arg.id = tiaacref.createId();
		}
		if (typeof arg.label !== 'string') {
			arg.label = defaults.label;
		}
		if (typeof arg.color !== 'string') {
			arg.color = defaults.color;
		}
		if (typeof arg.negativeColor !== 'string') {
			arg.negativeColor = defaults.negativeColor;
		}
		if (typeof arg.display !== 'boolean') {
			arg.display = defaults.display;
		}
		if (typeof arg.exclude !== 'boolean') {
			arg.exclude = defaults.exclude;
		}
		if (typeof arg.onClick !== 'function') {
			arg.onClick = defaults.onClick;
		}
		if (!arg.tooltip instanceof tiaacref.tooltip) {
			arg.tooltip = defaults.tooltip;
		}
		if (typeof arg.format !== 'string') {
			arg.format = defaults.format;
		}
	}
//	if (typeof arg.value=='undefined') {
	if (typeof arg.value !== 'number' && arg.value != null) {
		throw tiaacref.error('typeError', 'value', 'numeric');
	}
	this.arg = arg;
	this.percentage = (typeof arg.percentage !== 'number' ? 100 : arg.percentage);
	this.value = this.setValue(this.arg.value);
	this.color = this.arg.color;
	this.negativeColor = this.arg.negativeColor;
	this.name = this.arg.name;
	this.tag = this.arg.tag;
	this.label = this.arg.label;
	this.name = this.arg.name;
	this.format = this.arg.format;
	this.series = null;
	return;
};
tiaacref.point.prototype.setValue = function(value) {
//	if (typeof value === 'undefined') {
	if (typeof value !== 'number' && value != null) {
		throw tiaacref.error('typeError', 'value', 'numeric');
	}
	this.value = value;
	if (this.series != null && this.series instanceof tiaacref.series) {
		this.series.recalculate();
	}
	return this.value;
};







/* ================================================== MENU ========================================= */
tiaacref.menu = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.menu,
		jq,
		li,
		self,
		selectedli,
		megaMenu,
		maxColumns,
//		ie8 = tiaacref.isIE().ie8;
		ie8 = false,
		ARROWRIGHT = 39,
		ARROWLEFT = 37,
		TAB = 9;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style,
			attachTo: defaults.attachTo
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof arg.attachTo !== 'string') {
			arg.attachTo = defaults.attachTo;
		}
	}
	jq = {
		id: $(arg.id)
	};
	this.jq = jq;
	if (jq.id.length === 0) {
		if (arg.attachTo === defaults.attachTo) {
			throw tiaacref.error('badId', arg.id, 'menu');
		}
		jq.id = $(arg.attachTo);
		if (jq.id.length === 0) {
			throw tiaacref.error('badId', arg.attachTo, 'menu');
		}
	}
	if (arg.style === 'megamenu') {
		jq.attachTo = $(arg.attachTo);
		if (typeof jq.attachTo.length === 0) {
			throw tiaacref.missingDom('badId', arg.attachTo, 'DOM for attachTo not found for megamenu');
		}
		this.jq = jq;
		this.attachToZIndex = jq.attachTo.css('z-index');
		if (jq.attachTo.find('.nav-icon').length == 0) {
//			jq.attachTo.append('<span class="icon icDropdown"></span>');
			jq.attachTo.append('<span class="nav-icon"></span>');
		}
		tcoIndependent.add(jq.id);
		if (ie8) {
			tcoDeferred.add(jq.id);
		}
		jq.attachTo.attr({
			'aria-label': jq.attachTo.attr('aria-label') + ' - use up and down arrows keys to access submenu.',
			'aria-haspopup': true
		});
		// count the columns
		megaMenu = jq.attachTo.parent().children('.megamenu').first();
		if (megaMenu.length === 1) {
			maxColumns = 0;
			megaMenu.children('.mm_row').each(function (index, row) {
				maxColumns = Math.max(maxColumns, $(row).children('.mm_col').length);
			});
			megaMenu.addClass('mm-' + maxColumns + 'col');
		}
		// l1nav and l2nav now being handled by TIAA.menu(), so skip our mouse/keyboard assignments
		if (jq.attachTo.parents('.l1nav,.l2nav').length === 0) {
			jq.attachTo.on('focus', {self: this}, function(event) {
				"use strict";
				event.data.self.close();
			});
			
			jq.attachTo.on('keydown', {self: this}, function(event) {
				"use strict";
				switch (event.which) {
				case 40:
					event.preventDefault();
					activateMegaMenu(event.data.self);
				}
			});
			
			jq.attachTo.mouseenter({self: this}, function(event) {
				"use strict";
				activateMegaMenu(event.data.self);
				return;
			});
			jq.attachTo.mouseleave({self: this}, function(e) {
				"use strict";
				e.data.self.close();
			});
		}
		return;
	}
	li = jq.id.children('li');
	this.li = li;
	this.arg = arg;
	this.tags = {};
	self = this;
	// select the element
	selectedli = li.filter('[data-selected="true"]');
	if (selectedli.length === 0) {
		selectedli = li.first();
	}
	// set up the clicks
	li.each(function (index) {
		"use strict";
		var innerli = $(this),
			dataTag = innerli.attr('data-tag'),
			dataFor = innerli.attr('data-for'),
			dataSelected = (innerli.attr('data-selected') === 'true' ? true : false),
			dataEmptyText = (innerli.attr('data-emptytext') === 'true' ? true : false),
			selectElement;
		self.tags[dataTag] = '';
		if (dataEmptyText) {
			selectElement = innerli;
		} else {
			selectElement = innerli.children('a');
		}
		selectElement.on('keydown', {self: self, li: innerli}, function(e) {
			"use strict";
			var self = e.data.self,
				li = e.data.li,
				newLi;
			switch (e.which) {
			case ARROWRIGHT:
				newLi = li.next();
				if (newLi.length > 0) {
					newLi.children('a').first().focus();
				}
				break;
			case ARROWLEFT:
				newLi = li.prev();
				if (newLi.length > 0) {
					newLi.children('a').first().focus();
				}
				break;
			default:
				break;
			}
		});
		selectElement.on('focus', {self: self, li: innerli}, function(e) {
			"use strict";
			tcoipc.fire(self.event('selectTab'), e.data.li.attr('data-tag'));
			e.data.self.select(e.data.li.attr('data-tag'));
		});
		selectElement.click({self: self, li: innerli}, function(e) {
			"use strict";
			$(this).focus();
		});
	});
	this.select(selectedli.attr('data-tag'));
	// set up listener
	tcoipc.on(this.getSelectEventName(), 
		function(payload) {
			self.select(payload);
			return;
		}
	);
	
	function activateMegaMenu(self) {
		"use strict";
		var megaMenuId = self.jq.id,
			id = self.jq.attachTo,
//			ie8 = tiaacref.isIE().ie8,
			ie8 = false,
			idLeft = id.offset().left,
			px = (ie8 ? idLeft : 0),
			megaMenuWidth,
			windowWidth,
			oldLeft;
		id.addClass('megamenuhover');
		id.css({'z-index': 111});
		tcoIndependent.clear();
		oldLeft = megaMenuId.offset().left;
		megaMenuId.css({width: 'auto', position: 'absolute', zIndex: 111, display: 'block'})
			.removeClass('hidden')
			.attr({'aria-hidden': false});
		megaMenuWidth = 0;
		megaMenuId.children().each(function(index) {
			var me = $(this);
			if (!me.is('.clear, .clearfix')) {
				megaMenuWidth += $(this).outerWidth(true);
			}
		}); 
		windowWidth = $(window).width();
		if (idLeft + megaMenuWidth > windowWidth) {
			if (ie8) {
				px = windowWidth - megaMenuWidth - 25;	// '25' is for the side bar, plus a little extra for IE6
			} else {
				px = windowWidth - megaMenuWidth - idLeft - 25;	// '25' is for the side bar, plus a little extra for IE6
			}
		}
		megaMenuId.css({left: px, width: megaMenuWidth});
		if (ie8) {
			megaMenuId.css({top: id.offset().top + id.outerHeight(true)});
		}
		tcoIndependent.setActive(tcoIndependent.STILLINANCHOR);
		tcoIndependent.operate(tiaacref.defaults.independentElement.shorterTimeout);
		megaMenuId.mouseleave({self: self}, function (ee) {
			"use strict";
			ee.data.self.close(true);
		});
		megaMenuId.mouseenter({self: self}, function (ee) {
			tcoIndependent.setActive(tcoIndependent.STILLININDEPENDENT);
			id.addClass('megamenuhover');
			id.css({'z-index': 111});
		});
		
		megaMenuId.on('keydown', {self: self}, function(event) {
			"use strict";
			var self = event.data.self;
			switch (event.which) {
			case 38:
				event.preventDefault();
				break;
			case 40:
				event.preventDefault();
				break;
			case 27:
				self.close(true);
				break;
			}
		});
		megaMenuId.find('a,input,select,button,textarea').first().focus();
		megaMenuId.find('a,input,button,select,textarea').on('focusout focusin', {self: this}, function(event) {
			"use strict";
			switch(event.type) {
			case 'focusout':
				tcoIndependent.setActive(tcoIndependent.LEFTANCHOR);
				break;
			case 'focusin':
				tcoIndependent.setActive(tcoIndependent.STILLININDEPENDENT);
				break;
			}
		});

		return;
	}
};
tiaacref.menu.prototype.close = function(focus) {
	"use strict";
	if (typeof focus !== 'boolean') {
		focus = false;
	}
	tcoIndependent.setActive(tcoIndependent.LEFTANCHOR);
	this.jq.attachTo.removeClass('megamenuhover');
	this.jq.attachTo.css({'z-index': self.attachToZIndex});
	if (focus === true) {
		this.jq.attachTo.focus();
	}
	
};
tiaacref.menu.prototype.select = function(tag) {
	"use strict";
	if (typeof tag !== 'string') {
		throw tiaacref.error('', 'tag argument in menu.select() must be a string');
	}
	if (typeof this.tags[tag] === 'undefined') {
		throw tiaacref.error('', 'cannot find tag argument in list of tags for menu.select().');
	}
	this.li.each(function(i) {
		"use strict";
		var li = $(this),
			dataEmptyText = (li.attr('data-emptytext') === 'true' ? true : false),
			dataFor = li.attr('data-for'),
			dataTag = li.attr('data-tag'),
			a,
			element;
		if (dataEmptyText) {
			element = li;
		} else {
			element = li.children('a');
		}
		if (dataTag === tag) {
			element.addClass('selected');
			$(dataFor).removeClass('hidden').attr({'data-selected': 'true'});
			$(dataFor).css({display: 'block'}); 
		} else {
			element.removeClass('selected');
			$(dataFor).addClass('hidden').attr({'data-selected': 'false'});
		}
	});
	return;
};
tiaacref.menu.prototype.getSelectEventName = function () {
	"use strict";
	var defaults = tiaacref.defaults.menu;
	return tiaacref.eventName('menu.select') + this.arg.id;
};
tiaacref.menu.prototype.getId = function() {
	"use strict";
	return this.arg.id;
};
tiaacref.menu.prototype.setTabSelected = function(tag) {
	"use strict";
	this.select(tag);
	return;
};
tiaacref.menu.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('menu.' + eventName) + this.arg.id;
};






/* ------------------------------------------------------ DROP DOWN MENU ---------------------------------------- */
tiaacref.dropDownMenu = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.dropDownMenu,
		jq,
		dropDownMenuId;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			dropDownMenuId: defaults.dropDownMenuId,
			detached: defaults.detached,
			position: defaults.position
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.dropDownMenuId !== 'string') {
			arg.dropDownMenuId = defaults.dropDownMenuId;
		}
		if (typeof arg.detached !== 'boolean') {
			arg.detached = defaults.detached;
		}
        if (typeof arg.position !== 'string') {
            arg.position = defaults.position;
	    }
	    if (isNaN(arg.position)) {
	       if (arg.position !== 'left' && arg.position !== 'right') {
	          arg.position = defaults.position;
	       }
	    } else {
	       arg.position = parseFloat(arg.position);
	    }

	}
	jq = {
		id: $(arg.id),
		dropDownMenuId: $(arg.dropDownMenuId)
	};
	if (jq.dropDownMenuId.length === 0) {
		throw new tiaacref.error('badId', arg.dropDownMenuId, 'dropDownMenu');
	}
	if (arg.detached === false) {
		if (jq.id.length === 0) {
			throw new tiaacref.error('badId', arg.id, 'dropDownMenu');
		}
	}
	this.dialogParent = jq.dropDownMenuId.parents('div.ui-dialog').first(),
	this.inDialog = this.dialogParent.length === 1 ? true : false;
	
	jq.dropDownMenuId.addClass('hidden');
	jq.dropDownMenuId.css({display: 'none'});
//	tcoDeferred.add(jq.dropDownMenuId);
	tcoIndependent.add(jq.dropDownMenuId,'dropdownmenu');
	this.jq = jq;
	this.arg = arg;
	this.setDetached(arg.detached);
};
tiaacref.dropDownMenu.prototype.setDetached = function(detachValue) {
	"use strict";
	var defaults = tiaacref.defaults.dropDownMenu,
		jq = this.jq,
		arg = this.arg,
		menuItems,
		keypress;
	if (typeof detachValue !== 'boolean') {
		throw new tiaacref.error('badId', detachValue, 'dropDownMenu.setDetached()');
	}
	if (detachValue === true) {
		jq.id.unbind('click');
		jq.id = null;
		jq.dropDownMenuId.addClass('hidden');
		jq.dropDownMenuId.css({display: 'none'});

	} else {
		if (tiaacref.isIE().ie8) {
			jq.dropDownMenuId.find('ul').first().children('li')
				.mouseenter({self: this}, function(e) {
					$(this).css({'background-color': '#dbf1fd', 'border-size': 1, 'border-style': 'solid', 'border-color': '#008DBE'});
				})
				.mouseleave({self: this}, function(e) {
					$(this).css({'background-color': '', 'border-size': '', 'border-style': '', 'border-color': ''});
				});
		}
		jq.dropDownMenuId.find('ul').attr({role: 'menu'});
		menuItems = jq.dropDownMenuId.find('ul').first().children('li');
		menuItems.attr({role: 'menuitem', tabindex: -1});
		menuItems.on('keydown', {self: this}, function(event) {
			"use strict";
			var self = event.data.self;
			switch(event.which) {
			case 40:
				event.preventDefault();
//				keypress = jQuery.Event('keypress');
//				keypress.ctrlKey = false;
//				keypress.shiftKey = false;
//				keypress.which = 9;
//				$(this).trigger(keypress);
				break;
			case 38:
				event.preventDefault();
				break;
			case 27:
				event.preventDefault();
				closeDropDownMenu(self);
				self.jq.id.focus();
			}
		});
		menuItems.find('a,input,button,select,textarea').on('focusout focusin', {self: this}, function(event) {
			"use strict";
			switch(event.type) {
			case 'focusout':
				tcoIndependent.setActive(tcoIndependent.LEFTANCHOR);
				break;
			case 'focusin':
				tcoIndependent.setActive(tcoIndependent.STILLININDEPENDENT);
				break;
			}
		});
		menuItems.find('a').click({self: this}, function(e) {
			"use strict";
			var defaults = tiaacref.defaults.dropDownMenu,
				self = e.data.self,
				element = $(this),
				dataValue = element.parent().attr('data-value');
			tcoIndependent.setActive(tcoIndependent.LEFTINDEPENDENT);
			tcoIndependent.clear();
			tcoipc.fire(self.getSelectOptionEventName(), (dataValue == undefined ? null : dataValue));
			if (element.attr('href').indexOf('javascript:') === 0) {
				return false;
			} else {
				return true;
			}
		});
		jq.id.on('keydown', {self: this}, function(event){
			"use strict";
			var self = event.data.self;
			if (event.which === 40) {
				event.preventDefault();
				activateDropDownMenu(event.data.self);
			}
		});
		jq.id.on('click', {self: this}, function(e) {
			"use strict";
			e.preventDefault();
			activateDropDownMenu(e.data.self);
		});
		jq.id.on('focus', {self: this}, function(event) {
			"use strict";
			closeDropDownMenu(event.data.self);
		});
		
	}
	this.arg.detached = detachValue;
	
	function closeDropDownMenu(self, delay) {
		"use strict";
		if (typeof delay !== 'boolean') {
			delay = false;
		}
		if (delay === true) {
			tcoIndependent.setActive(tcoIndependent.LEFTANCHOR);
		} else {
			tcoIndependent.clear();
			tcoIndependent.setActive(tcoIndependent.LEFTINDEPENDENT);			
		}
		if (self.inDialog) {
			self.dialogParent.css({overflow: 'hidden'});
		}
	}
	
	function activateDropDownMenu(self) {
		"use strict";
		var defaults = tiaacref.defaults.dropDownMenu,
			id = self.jq.id,
			dropDownMenuId = self.jq.dropDownMenuId,
			top = id.offset().top + id.outerHeight(true),
			cssLeft = parseFloat(id.css('marginLeft'));
		tcoIndependent.clear();
		if (self.inDialog) {
			self.dialogParent.css({overflow: 'visible'});
		}
		self.jq.dropDownMenuId.removeClass('hidden').attr({'aria-hidden': false})
		.css({
			position: 'absolute',
			display: 'block',
			top: id.position().top + id.outerHeight(true),
			left: (self.arg.position === 'right' ? id.position().left - (dropDownMenuId.outerWidth(true) - id.outerWidth(true)) : 
                (self.arg.position === 'left' ?  id.position().left + (isNaN(cssLeft) ? 0 : cssLeft) : id.position().left + self.arg.position))
		});


		tcoIndependent.setActive(tcoIndependent.STILLINANCHOR);
//		tcoIndependent.operate(tiaacref.defaults.independentElement.timeout);
		tcoIndependent.operate(tiaacref.defaults.independentElement.shorterTimeout);
		dropDownMenuId.find('a,input,select,textarea').first().focus();
		dropDownMenuId.mouseleave({self: self}, function(ee) {
			$(this).addClass('hidden').css({display: 'none'});				
			tcoIndependent.setActive(tcoIndependent.LEFTINDEPENDENT);
		});
		dropDownMenuId.mouseenter({self: self}, function (ee) {
			tcoIndependent.setActive(tcoIndependent.STILLININDEPENDENT);
		});
		id.mouseleave({self: self}, function (ee) {
			tcoIndependent.setActive(tcoIndependent.LEFTANCHOR);
		});
		return false;
	}
	
};


tiaacref.dropDownMenu.prototype.attachTo = function(id) {
	"use strict";
	var defaults = tiaacref.defaults.dropDownMenu,
		jq = this.jq,
		arg = this.arg,
		jId;
	if (typeof id !== 'string') {
		throw new tiaacref.error('badId', id, 'dropDownMenu.attachTo()');
	}
	if ($(jId).length === 0) {
		throw new tiaacref.error('badId', id, 'dropDownMenu.attachTo()');
	}
	this.detach();
	this.jq.id = $(jId);
	this.setDetached(false);
};
tiaacref.dropDownMenu.prototype.detach = function() {
	"use strict";
	this.setDetached(true);
};
tiaacref.dropDownMenu.prototype.getSelectOptionEventName = function () {
	"use strict";
	return tiaacref.defaults.dropDownMenu.eventName.selectOption + this.arg.id;
};

tiaacref.dropDownMenu.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('dropDownMenu.' + eventName) + this.arg.id;
};
/* ---------------------------------------------------------------- MESSAGE -------------------------------------------------- */
tiaacref.message = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.message,
		jq;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style,
			closeable: defaults.closeable
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof defaults.validStyle[arg.style] === 'undefined') {
			arg.style = defaults.style;
		}
		if (typeof arg.closeable !== 'boolean') {
			arg.closeable = defaults.closeable;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'message');
	}
	this.arg = arg;
	this.jq = jq;
	if (arg.closeable) {
		jq.id.find('a.closeLink').click({self: this}, function(e) {
			"use strict";
			var self = e.data.self;
			self.close();
			return false;
		});
	}
	return;
};
tiaacref.message.prototype.getId = function() {
	"use strict";
	return this.arg.id;
};
tiaacref.message.prototype.close = function(self) {
	"use strict";
	self = (typeof self === 'undefined' ? this : self);
	self.jq.id.addClass('hidden').removeClass('visible');
	return false;
};
tiaacref.message.prototype.hide = function() {
	"use strict";
	this.close();
};
tiaacref.message.prototype.clear = function() {
	"use strict";
	var messageBody = this.jq.id.find('.messageBodyContent');
	messageBody.empty().append($('<span>').addClass('icon'));
	this.close();
	return;
};
tiaacref.message.prototype.addMessage = function(message, insertType) {
	"use strict";
	var defaults = tiaacref.defaults.message,
		li;
	if (typeof insertType === 'undefined') {
		insertType = defaults.insertType;
	}
	if (typeof defaults.validInsertType[insertType] === 'undefined' ) {
		insertType = defaults.insertType;
	}
	switch (insertType) {
	case 'standard':
	default:
		this.jq.id.find('.messageBodyContent').append(message);
		break;
	case 'listing':
		li = $('<li>').append(message);
		this.jq.id.find('.messageBodyContent ul').append(li);
		break;
	}
	this.open();
	return;
};
tiaacref.message.prototype.changeStyle = function(style) {
	"use strict";
	var defaults = tiaacref.defaults.message,
		id = this.jq.id,
		visible = id.hasClass('visible'),
		hidden = id.hasClass('hidden');
	if (typeof style !== 'string') {
		throw tiaacref.error('badId', style, 'message.changeStyle');
	}
	if (typeof defaults.validStyle[style] === 'undefined') {
		throw tiaacref.error('badId', style, 'message.changeStyle');
	}
	id.removeClass().addClass(defaults.validStyle[style]);
	if (hidden === true) {
		id.addClass('hidden');
	}
	if (visible === true) {
		id.addClass('visible');
	}
	return;
};
tiaacref.message.prototype.open = function(timeToClose) {
	"use strict";
	var self = this;
	if (typeof timeToClose !== 'number') {
		timeToClose = 0;
	}
	this.jq.id.removeClass('hidden').addClass('visible');
	if (timeToClose !== 0) {
		setTimeout(function() {
			self.close(self);
		}, timeToClose);
	}
	return;
};
tiaacref.message.prototype.show = function() {
	"use strict";
	this.open();
};







/* -------------------------------------------------------- OPINION ------------------------------------ */
tiaacref.opinionDatabase = [];
tiaacref.opinion = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.opinion;
	if (typeof arg !== 'object') {
		arg = {
			libraryLocation: defaults.libraryLocation,
			configurationLocation: defaults.configurationLocation,
			active: defaults.active,
			feedback: defaults.feedback
		};
	} else {
		if (typeof arg.libraryLocation !== 'string') {
			arg.libraryLocation = defaults.libraryLocation;
		}
		if (typeof arg.configurationLocation !== 'string') {
			arg.configurationLocation = defaults.configurationLocation;
		}
		if (typeof arg.active !== 'boolean') {
			arg.active = defaults.active;
		}
		if (typeof arg.feedback !== 'boolean') {
			arg.feedback = defaults.feedback;
		}
	}
	this.arg = arg;
	this.addedToDataLayer = -1;
	this.attachedFunctions = [];
};
tiaacref.opinion.prototype.setActive = function (arg) {
	"use strict";
	if (typeof arg !== 'boolean') {
		throw tiaacref.error('badId', arg, 'opinion.setActive');
	}
	this.arg.active = arg;
	if (tiaacref.opinionLabId != undefined) {
		digitalData.component[tiaacref.opinionLabId].attributes.active = arg;
	}
	return;
};
tiaacref.opinion.prototype.getActive = function () {
	return this.arg.active;
};
tiaacref.opinion.prototype.setFeedback = function (arg) {
	"use strict";
	if (typeof arg !== 'boolean') {
		throw tiaacref.error('badId', arg, 'opinion.setFeedback');
	}
	this.arg.feedback = arg;
	return;
};
tiaacref.opinion.prototype.getFeedback = function () {
	return this.arg.feedback;
};
tiaacref.opinion.prototype.setLibraryLocation = function (arg) {
	"use strict";
	if (typeof arg !== 'string') {
		throw tiaacref.error('badId', arg, 'opinion.setLibraryLocation');
	}
	this.arg.libraryLocation = arg;
	return;
};
tiaacref.opinion.prototype.setConfigurationLocation = function (arg) {
	"use strict";
	if (typeof arg !== 'string') {
		throw tiaacref.error('badId', arg, 'opinion.setConfigurationLocation');
	}
	this.arg.configurationLocation = arg;
	return;
};
tiaacref.opinion.prototype.set = function (card, variable, value) {
	"use strict";
	tiaacref.opinionDatabase.push({
		card: card, 
		variable: variable, 
		value: value
	});
};
tiaacref.opinion.prototype.attach = function (attachFunction) {
	"use strict";
	if (typeof attachFunction === 'function') {
		this.attachedFunctions.push(attachFunction);
	}
	return;
};
tiaacref.opinion.prototype.toDataLayer = function (data) {
	if (this.addedToDataLayer === -1) {
		this.addedToDataLayer = tiaacref.adl.map('component', {componentName: 'Opinion Lab', componentID: 'OL' + tiaacref.createId()});
	}
	$.extend(digitalData.component[this.addedToDataLayer].attributes, data);
};

tiaacref.opinion.prototype.load = function () {
	"use strict";
	var self = this,
		i,
		l;
	if (!tiaacref.useEnsighten) {
		if (this.arg.active && this.arg.libraryLocation !== '') {
			$(window).load(function() {
				$.ajax({
					type: 'GET',
					url: self.arg.libraryLocation,
					data: null,
					success: function() {
						loadConfiguration();
					},
					error: function() {
						throw tiaacref.error('', 'Opinion load error. Failed to load library script with AJAX.');
					},
					dataType: 'script',
					cache: true,
					crossDomain: true
				});
			});
		}
	}
	return;
	
	function loadConfiguration() {
		"use strict";
		if (self.arg.configurationLocation !== '') {
			$.ajax({
				type: 'GET',
				url: self.arg.configurationLocation,
				data: null,
				success: function() {
					loadVariables();
					l = self.attachedFunctions.length;
					for (i = 0; i < l; i++) {
						self.attachedFunctions[i]();
					}
				},
				error: function() {
					throw tiaacref.error('', 'Opinion load error. Failed to load configuration script with AJAX.');
				},
				dataType: 'script',
				cache: true,
				crossDomain: true
			});
		}
	}
// TODO: Eval is Evil	
	function loadVariables() {
		"use strict";
		var i,
			l = tiaacref.opinionDatabase.length,
			opinion;
		for (i = 0; i < l; i++) {
			opinion = tiaacref.opinionDatabase[i];
			eval(opinion.card + '.options.customVariables.' + opinion.variable + ' = "' + opinion.value + '";');
		}
		return;
	}
};
tiaacrefOpinion = new tiaacref.opinion();



/* -------------------------------------------------------- GOMEZ ------------------------------------ */
tiaacref.gomezDatabase = [];
tiaacref.gomez = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.gomez;
	if (typeof arg !== 'object') {
		arg = {
			libraryLocation: defaults.libraryLocation,
			active: defaults.active
		};
	} else {
		if (typeof arg.libraryLocation !== 'string') {
			arg.libraryLocation = defaults.libraryLocation;
		}
		if (typeof arg.active !== 'boolean') {
			arg.active = defaults.active;
		}
	}
	this.arg = arg;
	this.loaded = false;
	this.addedToDataLayer = -1;
	this.attachedFunctions = [];
};
tiaacref.gomez.prototype.setActive = function (arg) {
	"use strict";
	if (typeof arg !== 'boolean') {
		throw tiaacref.error('badId', arg, 'gomez.setActive');
	}
	this.arg.active = arg;
	return;
};
tiaacref.gomez.prototype.getActive = function () {
	return this.arg.active;
};
tiaacref.gomez.prototype.setLibraryLocation = function (arg) {
	"use strict";
	if (typeof arg !== 'string') {
		throw tiaacref.error('badId', arg, 'gomez.setLibraryLocation');
	}
	this.arg.libraryLocation = arg;
	return;
};
tiaacref.gomez.prototype.set = function (variable, value) {
	"use strict";
	tiaacref.gomezDatabase.push({
		variable: variable, 
		value: value
	});
};
tiaacref.gomez.prototype.attach = function (attachObject, attachFunction) {
	"use strict";
	if (typeof attachFunction === 'function') {
		this.attachedFunctions.push({attachObject: attachObject, attachFunction: attachFunction});
	}
	return;
};
tiaacref.gomez.prototype.toDataLayer = function (data) {
	if (this.addedToDataLayer === -1) {
		this.addedToDataLayer = tiaacref.adl.map('component', {componentName: 'Gomez', componentID: 'G' + tiaacref.createId()});
		if (tiaacrefPageTime) {
			$.extend(digitalData.component[this.addedToDataLayer].attributes, {gs: tiaacrefPageTime});
		}
	}
	$.extend(digitalData.component[this.addedToDataLayer].attributes, data);
};

tiaacref.gomez.prototype.load = function (originalObject) {
	"use strict";
	var self = this,
		gomezPrefix,
		domain,
		subDomain,
		subSubDomain,
		pageName,
		changedSelf = false,
		i,
		l,
		pgIdHit,
		isS_omtr = (s_omtr != null);
	if (originalObject instanceof tiaacref.gomez) {
		self = originalObject;
		changedSelf = true;
	}
	if (self.loaded === true) {
		return;
	}
	pgIdHit = -1;
	l = tiaacref.gomezDatabase.length;
	for (i = 0; i < l; i ++) {
		if (tiaacref.gomezDatabase[i].variable === 'pgId') {
			pgIdHit = i;
			pageName = tiaacref.gomezDatabase[i].value; 
		}
	}
	if (pgIdHit === -1) {
		if (isS_omtr) {
			pageName = s_omtr.pageName;
		} else if (typeof tiaacref.analyticsDatabase.pageName === 'undefined') {
			pageName = window.location.hostname + window.location.pathname;
		}else {
			pageName = tiaacref.analyticsDatabase.pageName;
		}
	}
	self.set('pgId', tiaacref.serverEnvironment().gomezTag + pageName);
	if (self.arg.active && self.arg.libraryLocation !== '' && !tiaacref.useEnsighten) {
		loadVariables();
		self.loaded = true;
		if (changedSelf === false) {
			$(document).ready(function() {
				$.ajax({
					type: 'GET',
					url: self.arg.libraryLocation,
					data: null,
					success: function() {
						l = self.attachedFunctions.length;
						for (i = 0; i < l; i++) {
							self.attachedFunctions[i].attachFunction(self.attachedFunctions[i].attachObject);
						}
						tcoipc.fire('gomezLoad', {});
					},
					error: function() {
						throw tiaacref.error('', 'Gomez load error. Failed to load library script with AJAX.');
					},
					dataType: 'script',
					cache: true,
					crossDomain: true
				});
			});
		} else {
			$.ajax({
				type: 'GET',
				url: self.arg.libraryLocation,
				data: null,
				success: function() {
					l = self.attachedFunctions.length;
					for (i = 0; i < l; i++) {
						self.attachedFunctions[i].attachFunction(self.attachedFunctions[i].attachObject);
					}
					tcoipc.fire('gomezLoad', {});
				},
				error: function() {
					throw tiaacref.error('', 'Gomez load error. Failed to load library script with AJAX.');
				},
				dataType: 'script',
				cache: true,
				crossDomain: true
			});
		}
	}
	return;
	
	function loadVariables() {
		"use strict";
		var i,
			l = tiaacref.gomezDatabase.length;
		for (i = 0; i < l; i++) {
			gomez[tiaacref.gomezDatabase[i].variable] = tiaacref.gomezDatabase[i].value;
		}
		return;
	}
};
tiaacrefGomez = new tiaacref.gomez();













/* ----------------------------------------------- Animated Bar -------------------------------------------- */
tiaacref.animatedBar = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.animatedBar,
		jq,
		self;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style,
			value: defaults.value
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof defaults.validStyle[arg.style] === 'undefined') {
			arg.style = defaults.style;
		}
		arg.value = parseFloat(arg.value);
		if (isNaN(arg.value)) {
			arg.value = defaults.value;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'animatedBar');
	}
	if (arg.value < 0 || arg.value > 100) {
		throw tiaacref.error('', 'Value argument must be between 0 and 100 inclusive (animatedBar)');
	}
	this.arg = arg;
	this.jq = jq;
	self = this;
	jq.id.progressbar({});
	this.setValue(arg.value);
	// events
	tcoipc.on(this.getSetValueEventName(), function(payload) {
		"use strict";
		self.setValue(payload);
	});
	return;
};
tiaacref.animatedBar.prototype.setValue = function(value) {
	"use strict";
	value = parseFloat(value);
	if (value < 0 || value > 100) {
		throw tiaacref.error('', 'Value argument must be between 0 and 100 inclusive (animatedBar.setValue)');
	}
	this.arg.value = value;
	this.jq.id.progressbar('option', 'value', value);
	tcoipc.fire(this.getValueChangeEventName(), value);
	return;
};
tiaacref.animatedBar.prototype.getValue = function() {
	"use strict";
	return this.arg.value;
};
tiaacref.animatedBar.prototype.getSetValueEventName = function() {
	"use strict";
	return tiaacref.defaults.animatedBar.eventName.setValue + this.arg.id;
};
tiaacref.animatedBar.prototype.getValueChangeEventName = function() {
	"use strict";
	return tiaacref.defaults.animatedBar.eventName.valueChange + this.arg.id;
};
tiaacref.animatedBar.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('animatedBar.' + eventName) + this.arg.id;
};






/* ------------------------------------------------ SLIDER ------------------------------------------------- */
tiaacref.slider = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.slider,
		jq,
		self,
		validTypeDate = defaults.validType.date,
		validTypeNumeric = defaults.validType.numeric,
		defaultFormat,
		dateTemp;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			startingValue: defaults.validType.numeric.selectedValue.start,
			endingValue: defaults.validType.numeric.selectedValue.end,
			minimumValue: defaults.validType.numeric.minimum,
			maximumValue: defaults.validType.numeric.maximum,
			style: defaults.style,
			type: defaults.type,
			format: defaults.format
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof defaults.validStyle[arg.style] === 'undefined') {
			arg.style = defaults.style;
		}
		if (typeof arg.type !== 'string') {
			arg.type = defaults.type;
		}
		if (typeof defaults.validType[arg.type] === 'undefined') {
			arg.type = defaults.type;
		}
		if (typeof arg.format !== 'string') {
			arg.format = defaults.format;
		}
		if (typeof defaults.validFormat[arg.format] === 'undefined') {
			arg.format = defaults.format;
		}
	}
	jq = {id: $(arg.id)};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'slider');
	}
	switch(arg.type) {
	case 'date':
		var defaultSlider = defaults.validFormat;
		defaultSlider.day.minimumValue = Date.today().add({days: -4});
		defaultSlider.day.maximumValue = Date.today().add({days: 5});

		defaultSlider.month.minimumValue = Date.today().moveToFirstDayOfMonth().add({months: -4});
		defaultSlider.month.maximumValue = Date.today().moveToFirstDayOfMonth().add({months: 5});

		defaultFormat = defaults.validFormat[arg.format];
		arg.minimumValue = Date.parseExact(arg.minimumValue, defaultFormat.parseExactFormat);
		if (arg.minimumValue == null) {
			arg.minimumValue = defaultFormat.minimumValue;
		}
		arg.maximumValue = Date.parseExact(arg.maximumValue, defaultFormat.parseExactFormat);
		if (arg.maximumValue == null) {
			arg.maximumValue = defaultFormat.maximumValue;
		}
		arg.startingValue = Date.parseExact(arg.statingValue, defaultFormat.parseExactFormat);
		if (arg.startingValue == null) {
			arg.startingValue = defaultFormat.minimumValue;
		}
		arg.endingValue = Date.parseExact(arg.endingValue, defaultFormat.parseExactFormat);
		if (arg.endingValue == null) {
			arg.endingValue = defaultFormat.maximumValue;
		}
		if (arg.startingValue.isAfter(arg.minimumValue)) {
			arg.startingValue = arg.minimumValue;
		}
		if (arg.endingValue.isBefore(arg.startingValue)) {
			arg.endingValue = arg.maximumValue;
		}
		if (arg.minimumValue.compareTo(arg.maximumValue) >= 0) {
			throw tiaacref.error('', 'minimumvalue greater than or equal to maximumvalue in date slider');
		}
		if (arg.startingValue.compareTo(arg.endingValue) >= 0) {
			throw tiaacref.error('', 'startingvalue greater than or equal to endingvalue in date slider');
		}
		this.working = {
			minimum: 0,
			maximum: tiaacref.date[arg.format].diff(arg.minimumValue, arg.maximumValue),
			starting: tiaacref.date[arg.format].diff(arg.minimumValue, arg.startingValue),
			ending: tiaacref.date[arg.format].diff(arg.endingValue, arg.minimumValue)
		};
		break;
	case 'numeric':
	default:
		if (typeof arg.startingValue !== 'string') {
			arg.startingValue = validTypeNumeric.selectedValue.start;
		} else {
			arg.startingValue = parseFloat(arg.startingValue);
			if (isNaN(arg.startingValue)) {
				arg.startingValue = validTypeNumeric.selectedValue.start;
			}
		}
		if (typeof arg.endingValue !== 'string') {
			arg.endingValue = validTypeNumeric.selectedValue.end;
		} else {
			arg.endingValue = parseFloat(arg.endingValue);
			if (isNaN(arg.endingValue)) {
				arg.endingValue = validTypeNumeric.selectedValue.end;
			}
		}
		if (typeof arg.minimumValue !== 'string') {
			arg.minimumValue = validTypeNumeric.minimumValue;
		} else {
			arg.minimumValue = parseFloat(arg.minimumValue);
			if (isNaN(arg.minimumValue)) {
				arg.minimumValue = validTypeNumeric.minimumValue;
			}
		} 
		if (typeof arg.maximumValue !== 'string') {
			arg.maximumValue = validTypeNumeric.maximumValue;
		} else {
			arg.maximumValue = parseFloat(arg.maximumValue);
			if (isNaN(arg.maximumValue)) {
				arg.maximumValue = validTypeNumeric.maximumValue;
			}
		}
		if (arg.startingValue < arg.minimumValue) {
			arg.startingValue = arg.minimumValue;
		}
		if (arg.endingValue > arg.maximumValue) {
			arg.endingValue = arg.maximumValue;
		}
		this.step = 1;
		this.working = {
			minimum: arg.minimumValue,
			maximum: arg.maximumValue,
			starting: arg.startingValue,
			ending: arg.endingValue
		};
		break;
	};
	this.jq = jq;
	this.arg = arg;
	this.selectedValue = {
		start: this.working.starting,
		end: this.working.ending
	};
	self = this;
	jq.id.slider({
		range: (arg.style === 'range' ? true : false),
		min: this.working.minimum,
		max: this.working.maximum,
		values: (this.arg.style === 'single' ? this.working.starting : [this.working.starting, this.working.ending]),
		step: this.step,
		slide: function(event, ui) {
			"use strict";
			var sliderName,
				style = self.arg.style,
				value = ui.value;
			if (style === 'single') {
				self.selectedValue.start = ui.value;
				sliderName = "single";
			} else {
				self.selectedValue.start = ui.values[0];
				self.selectedValue.end = ui.values[1];
				if (ui.value === ui.values[0]) {
					sliderName = 'start';
				} else if (ui.value === ui.values[1]) {
					sliderName = 'end';
				} else {
					sliderName = 'unknown';
				}
			}
			tcoipc.fire(self.getChangeSlideEventName(), {
				slider: sliderName,
				value: value,
				selectedValue: self.getSliderValues(),
				event: event, 
				ui: ui
			});
		}
	});
};
tiaacref.slider.prototype.getId = function() {
	"use strict";
	return this.arg.id;
};
tiaacref.slider.prototype.getChangeSlideEventName = function () {
	"use strict";
	return tiaacref.eventName('slider.changeSlide') + this.getId();
};
tiaacref.slider.prototype.getSliderValues = function () {
	"use strict";
	var defaultFormat,
		retval,
		startDate,
		endDate;
	switch(this.arg.type) {
	case 'date':
		retval = {
			minimumValue: '',
			maximumValue: '',
			startingValue: '',
			endingValue: '',
			start: '',
			end: '',
			startDate: {
				day: 0,
				month: 0,
				year: 0,
				date: null
			},
			endDate: {
				day: 0,
				month: 0,
				year: 0,
				date: null
			}
		};
		defaultFormat = tiaacref.defaults.slider.validFormat[this.arg.format];
		retval.minimumValue = this.arg.minimumValue.toString(defaultFormat.parseExactFormat);
		retval.maximumValue = this.arg.maximumValue.toString(defaultFormat.parseExactFormat);
		retval.startingValue = this.arg.startingValue.toString(defaultFormat.parseExactFormat);
		retval.endingValue = this.arg.endingValue.toString(defaultFormat.parseExactFormat);
		switch(this.arg.format) {
		case 'day':
			startDate = new Date(this.arg.minimumValue).add({days: this.selectedValue.start});
			endDate = new Date(this.arg.minimumValue).add({days: this.selectedValue.end});
			break;
		case 'month':
			startDate = new Date(this.arg.minimumValue).add({months: this.selectedValue.start});
			endDate = new Date(this.arg.minimumValue).add({months: this.selectedValue.end});
			break;
		}
		retval.startDate = {
			day: startDate.getDate(),
			month: startDate.getMonth(),
			year: startDate.getFullYear()
		};
		retval.endDate = {
			day: endDate.getDate(),
			month: endDate.getMonth(),
			year: endDate.getFullYear()
		};
		retval.startDate.date = startDate;
		retval.endDate.date = endDate;
		retval.start = startDate.toString(defaultFormat.parseExactFormat);
		retval.end = endDate.toString(defaultFormat.parseExactFormat);
		break;
	case 'numeric':
		retval = {
			minimumValue: this.arg.minimumValue,
			maximumValue: this.arg.maximumValue,
			startingValue: this.arg.startingValue,
			endingValue: this.arg.endingValue,
			start: this.selectedValue.start,
			end: this.selectedValue.end,
			startDate: {
				day: 0,
				month: 0,
				year: 0,
				date: null
			},
			endDate: {
				day: 0,
				month: 0,
				year: 0,
				date: null
			}
		};
		break;
	}
	return retval;
};
tiaacref.slider.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('slider.' + eventName) + this.arg.id;
};






/* ============================================= ENHANCED SELECT ======================================= */
tiaacref.enhancedSelect = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.enhancedSelect,
		jq,
		self,
		autoSelect = defaults.validType.autoselect,
		table,
		thead,
		tbody,
		tr,
		td,
		th,
		tid,
		size;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			type: defaults.type,
			size: defaults.size,
			selectText: defaults.selectText
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.type !== 'string') {
			arg.type = defaults.type;
		}
		if (typeof defaults.validType[arg.type] === 'undefined') {
			arg.type = defaults.type;
		}
		arg.size = parseInt(arg.size);
		if (!tiaacref.isNumber(arg.size)) {
			arg.size = defaults.size;
		}
		if (typeof arg.selectText !== 'string') {
			arg.selectText = defaults.selectText;
		}
	}
	jq = {
		id: $(arg.id),
		select: null,
		chosen: null,
		dropDown: null
	};
	if (jq.id.length === 0) {
		throw tiaacref.error('badId', arg.id, 'enhancedSelect');
	}
	this.jq = jq;
	this.arg = arg;
	this.data = [];
	self = this;
	switch (arg.type) {
	case 'standard':
	default:
		jq.select = jq.id.children('select');
		if (jq.select.length === 0) {
			throw tiaacref.error('badId', arg.id, 'enahancedSelect');
		}
		arg.selectId = arg.id + autoSelect.suffixes.select;
		arg.chosenId = arg.id + autoSelect.suffixes.chosen;
		jq.id.width(arg.size);
		jq.select.attr({'data-id': arg.selectId}).addClass(autoSelect.cssClass.select);
		jq.chosen = $('<div>').attr({'data-id': arg.chosenId}).addClass(autoSelect.cssClass.chosen).width(arg.size);
		table = $('<table>');
		thead = $('<thead>').append($('<tr>').append($('<th>')).append($('<th>').attr({'data-id': arg.chosenId + 'sort'}).addClass('header').text('Name')));
		table.append(thead);
		tbody = $('<tbody>').addClass('chzn-results');
		jq.select.children('option').each(function(index, option) {
			tid = arg.selectId + '_' + $(option).val();
			tr = $('<tr>').attr({id: tid}).addClass('active-result')
				.append($('<td>').append($('<input>').attr({type: 'checkbox', name: 'chosen', value: tid, 'data-index': index})))
				.append($('<td>').text($(option).text()));
			tbody.append(tr);
			self.data.push({checked: false, value: tid, text: $(option).text(), index: index});
		});
		table.append(tbody);
		
		jq.chosen.append($('<a>').attr({href: 'javascript:void(0);'}).addClass('chzn-single')
			.append($('<span>').html(arg.selectText + '<span class="chzn-selectedNumber"></span>'))
			.append('<div><b></b></div>')
		);
		jq.dropDown  = $('<div>').addClass('chzn-drop hidden').width(arg.size - 2)
			.append($('<div>').addClass('chzn-search')).append($('<input>').attr({type: 'text'}))
			.append($('<div>').addClass('chzn-results-container')
				.append(table)
		);
		jq.chosen.append(jq.dropDown);
		jq.id.append(jq.chosen);
		jq.dropDown.css({top: jq.chosen.height(), left: 0});
		jq.select.addClass('hidden');
		jq.chosen.children('a').first().click({self: this}, function(e) {
			e.stopPropagation();
			if (jq.dropDown.hasClass('hidden')) {
				$(this).addClass('chzn-single-with-drop');
				jq.dropDown.removeClass('hidden');
// TODO				tcoClickOutside.add(jq.dropDown);
			} else {
				$(this).removeClass('chzn-single-with-drop');
				jq.dropDown.addClass('hidden');
			}
		});
		jq.dropDown.find('input[type="text"]').width(jq.dropDown.width() - 6);
		_setEvents();
		jq.dropDown.find('input[type="text"]').keyup({self: this}, function(e) {
			"use strict";
			var self = e.data.self,
				text = $(this).val(),
				tbody = self.jq.dropDown.find('table tbody'),
				tr = tbody.children('tr'),
				td;
			if (text === '') {
				tr.removeClass('hidden');
			} else {
				td = tr.find('td:contains("' + $(this).val() + '")');
				tr.addClass('hidden');
				td.each(function(index, item) {
					$(this).parent().removeClass('hidden');
				});
			}
		});
		jq.dropDown.find($('th[data-id="' + arg.chosenId + 'sort' + '"]')).click({self: this}, function(e) {
			"use strict";
			var self = e.data.self,
				th = $(this),
				table,
				tr,
				i,
				l = self.data.length,
				sortValue = (th.hasClass('headerSortUp') ? 'desc' : 'asc');
			e.stopPropagation();
			th.addClass(sortValue == 'asc' ? 'headerSortUp' : 'headerSortDown').removeClass(sortValue == 'asc' ? 'headerSortDown' : 'headerSortUp');
			if (sortValue == 'asc') {
				self.data.sort(_sortAsc);
			} else {
				self.data.sort(_sortDesc);
			}
			tbody = $('<tbody>').addClass('chzn-results');
			for(i = 0; i < l; i++) {
				tr = $('<tr>').attr({id: self.data[i].value}).addClass('active-result')
					.append($('<td>')
						.append($('<input>')
							.attr({type: 'checkbox', name: 'chosen', value: self.data[i].value, 'data-index': self.data[i].index})
							.prop('checked', self.data[i].checked)
						)
					)
					.append($('<td>').text(self.data[i].text));
				tbody.append(tr);
			};
			table = self.jq.dropDown.find('table');
			table.find('tbody').remove();
			table.append(tbody);
			_setEvents();
		});
		this.jq = jq;
		break;
	}
	function _sortAsc(val1, val2) {
		if (val1.text < val2.text) {
			return -1;
		}
		if (val1.text > val2.text) {
			return 1;
		}
		return 0;
	};
	function _sortDesc(val1, val2) {
		if (val1.text < val2.text) {
			return 1;
		}
		if (val1.text > val2.text) {
			return -1;
		}
		return 0;
	}
	function _setEvents() {
		self.jq.dropDown.find(':checkbox').change({self: self}, function(e) {
			"use strict";
			e.stopPropagation();
			var self = e.data.self,
				chosen = self.jq.chosen,
				checked = chosen.find(':checked').length,
				checkbox = $(this),
				isChecked = checkbox.prop('checked'),
				checkboxIndex = parseInt(checkbox.attr('data-index')),
				selectedNumber = chosen.find('span.chzn-selectedNumber'),
				i;
			for (i in self.data) {
				if (checkboxIndex === self.data[i].index) {
					self.setValue(self.data[i].value, isChecked);
				}
			}
			if (selectedNumber.length > 0) {
				if (checked === 0) {
					selectedNumber.text('');
				} else {
					selectedNumber.text(' (' + checked + ' selected)');
				}
			}
			tcoipc.fire(self.getCheckedEventName(), self.getValues());
		});
	}
};
tiaacref.enhancedSelect.prototype.getId = function () {
	"use strict";
	return this.arg.id;
};
tiaacref.enhancedSelect.prototype.getValues = function () {
	"use strict";
	var values = this.jq.dropDown.find(':checked'),
		retval = {count: values.length, name: values.attr('name'), values: []};
	values.each(function(i) {
		retval.values.push($(this).val());
	});
	return retval;
};
tiaacref.enhancedSelect.prototype.setValue = function(tag, value) {
	"use strict";
	var i,
		hit = -1;
	for (i in this.data) {
		if (this.data[i].value === tag) {
			hit = this.data[i].index;
			this.data[i].checked = value;
		}
	}
	if (hit !== -1) {
		this.jq.dropDown.find('input[data-index="' + hit + '"]').prop('checked', value);
	}
	return;
};
tiaacref.enhancedSelect.prototype.getCheckedEventName = function () {
	"use strict";
	return tiaacref.eventName('enhancedSelect.checked') + this.getId();
};
tiaacref.enhancedSelect.prototype.getSetCheckBoxEventName = function () {
	"use strict";
	return tiaacref.eventName('enhancedSelect.setCheckBox') + this.getId();
};
tiaacref.enhancedSelect.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('enhancedSelect.' + eventName) + this.arg.id;
};









/* ---------------------------------------------- container -------------------------------------------- */
tiaacref.container = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.container,
		jq,
		self;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
	}
	jq = {
		id: $(arg.id)
	};
	if (jq.id.length === 0) {
		throw new tiaacref.missingDOM("Missing DOM element named '" + arg.id + "'.");
	}
	this.jq = jq;
	this.arg = arg;
	self = this;
	if (this.arg.style == 'fastquotebar') {
		$(document).ready(function() {
			$('body').append(self.jq.id);
			$('footer').parent().css({'padding-bottom': self.jq.id.height()});
		});
	}
	return;
};
tiaacref.container.prototype.getId = function() {
	"use strict";

	return this.arg.id;
};







/* --------------------------------------------------- INPUT -------------------------------------------------------- */
/*
tiaacref.input = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.input,
		jq,
		xhr,
		iFrame;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			type: defaults.type,
			compareTo: defaults.compareTo,
			ajaxTargetId: defaults.ajaxTargetId
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.type !== 'string') {
			arg.type = defaults.type;
		}
		if (typeof defaults.validType[arg.type] === 'undefined') {
			arg.type = defaults.type;
		}
		if (typeof arg.ajaxTargetId !== 'string') {
			arg.ajaxTargetId = defaults.ajaxTargetId;
		}
	}
	jq = {
			id: $(arg.id)
		};
	if (jq.id.length === 0) {
		throw new missingDOM("Missing DOM id '" + arg.id + "'.");
	}
	this.arg = arg;
	this.jq = jq;
	this.includedExtensions = defaults.includedExtensions;
	this.excludedExtensions = defaults.excludedExtensions;
	if (arg.type === 'ajaxfile') {
		jq.ajaxFormId = $(jq.id[0].form);
		if (jq.ajaxFormId.length === 0) {
			throw new tiaacref.missingDOM("Missing DOM id '" + arg.ajaxFormId + "'.");
		}
		arg.ajaxTargetId="#" + jq.ajaxFormId.attr('target');
		
		jq.ajaxTargetId = $(arg.ajaxTargetId);
		if (jq.ajaxTargetId.length === 0) {
			throw new tiaacref.missingDOM("Missing DOM id '" + arg.ajaxTargetId + "'.");
		}
		this.jq = jq;
		this.result = '';
		this.activeSubmit = false;
		jq.ajaxFormId.attr({target: arg.ajaxTargetId.substr(1)});
		try {
			xhr = new XMLHttpRequest();
		} catch (e) {
			xhr = "";
		}
		if (typeof xhr.upload === 'undefined') {
			jq.ajaxTargetId.load({self: this}, function(e) {			
				"use strict";
				var self = e.data.self,
					result = document.getElementById(self.arg.ajaxTargetId.substr(1)),
					frameData = null;
				if (result.contentWindow && result.contentWindow.document.body) {
					frameData = result.contentWindow.document.body;
				} else if (result.document && result.document.body) {
					frameData = result.document.body;
				} else if (result.contentDocument && result.contentDocument.body) {
					frameData = result.contentDocument.body;
				} else {
					throw new tiaacref.error('Bad return code from ajaxfile. Cannot access the iFrame.');
				}
				if (self.activeSubmit) {
					self.result = frameData.innerHTML;
					if (typeof self.arg.onfinish === 'function') {
						self.arg.onfinish();
					}
					tcoipc.fire(self.event('uploadFinished'), {});
					if (self.arg.animatedBar instanceof tiaacref.animatedBar) {
						self.arg.animatedBar.setValue(100);
					}
					self.activeSubmit = false;
				}
			});
		}
		jq.ajaxFormId.find('input[type="submit"]').first().on('click', {self: this}, function(e) {
			var self = e.data.self,
				fileData;
			self.activeSubmit = true;
			if (typeof self.arg.onstart === 'function') {
				self.arg.onstart();
			}
			tcoipc.fire(self.event('uploadStarted'), {});
			if (self.arg.animatedBar instanceof tiaacref.animatedBar) {
				self.arg.animatedBar.setValue(0);
			}
			if (typeof xhr.upload === 'undefined') {
				$(this).submit();
			} else {
				e.preventDefault();
				xhr = new XMLHttpRequest();
				xhr.open('POST', jq.ajaxFormId.attr('action'), true);

				xhr.upload.onprogress = function(uploadE) {
					tcoipc.fire(self.event('uploadInProgress'), {total: uploadE.total, loaded: uploadE.loaded});
					if (typeof self.arg.onprogress === 'function') {
						self.arg.onprogress({total: uploadE.total, loaded: uploadE.loaded});
					}
					if (self.arg.animatedBar instanceof tiaacref.animatedBar) {
						self.arg.animatedBar.setValue((uploadE.loaded / uploadE.total) * 100);
					}
				};

				xhr.onload = function () {
					if (typeof self.arg.onfinish === 'function') {
						self.arg.onfinish();
					}
					tcoipc.fire(self.event('uploadFinished'), {});
					if (self.arg.animatedBar instanceof tiaacref.animatedBar) {
						self.arg.animatedBar.setValue(100);
					}
				};
				fileData = new FormData();
				fileData.append('file', self.jq.id.get(0).files[0]);
				xhr.send(fileData);
			}
		});
	}
	

};
tiaacref.input.prototype.submit = function () {
	"use strict";
	if (this.arg.type !== "ajaxfile") {
		return;
	}
	this.activeSubmit = true;
	this.arg.onstart();
	this.jq.ajaxFormId.submit();
	return;
};

tiaacref.input.prototype.cancel = function () {
	"use strict";
	var result = document.getElementById(this.arg.ajaxTargetId.substr(1)),
		frameData = null;
	if (this.arg.type !== "ajaxfile") {
		return;
	}
	this.activeSubmit = false;
	if (result.contentWindow && result.contentWindow.document.body) {
		frameData = result.contentWindow.document.execCommand("Stop");
	} else if (result.document && result.document.body) {
		frameData = result.document.execCommand("Stop");;
	} else if (result.contentDocument && result.contentDocument.body) {
		frameData = result.contentDocument.execCommand("Stop");;
	} else {
		throw new tiaacref.error('Bad return code from ajaxfile. Cannot access the iFrame.');
	}
	
};
tiaacref.input.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('input.' + eventName) + this.arg.id;
};



tiaacref.input.prototype.setExcludedExtensions = function (fileList) {
	"use strict"
};
tiaacref.input.prototype.getExcludedExtensions = function () {
	"use strict"
};
tiaacref.input.prototype.addExcludedExtensions = function (fileList) {
	"use strict"
};
tiaacref.input.prototype.removedExcludedExtensions = function (fileList) {
	"use strict"
};
*/


tiaacref.input = function(arg) {
	"use strict";
	var defaults = tiaacref.defaults.input,
		jq,
		iFrame;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			type: defaults.type,
			compareTo: defaults.compareTo,
			ajaxTargetId: defaults.ajaxTargetId
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.type !== 'string') {
			arg.type = defaults.type;
		}
		if (typeof defaults.validType[arg.type] === 'undefined') {
			arg.type = defaults.type;
		}
		if (typeof arg.ajaxTargetId !== 'string') {
			arg.ajaxTargetId = defaults.ajaxTargetId;
		}
	}
	jq = {
			id: $(arg.id)
		};
	if (jq.id.length === 0) {
		throw new missingDOM("Missing DOM id '" + arg.id + "'.");
	}
	this.arg = arg;
	this.jq = jq;
	if (arg.type === 'ajaxfile') {
		jq.ajaxFormId = $(jq.id[0].form);
		if (jq.ajaxFormId.length === 0) {
			throw new tiaacref.missingDOM("Missing DOM id '" + arg.ajaxFormId + "'.");
		}
		arg.ajaxTargetId="#" + jq.ajaxFormId.attr('target');
		
		jq.ajaxTargetId = $(arg.ajaxTargetId);
		if (jq.ajaxTargetId.length === 0) {
			throw new tiaacref.missingDOM("Missing DOM id '" + arg.ajaxTargetId + "'.");
		}
		this.jq = jq;
		this.result = '';
		this.activeSubmit = false;
		jq.ajaxFormId.attr({target: arg.ajaxTargetId.substr(1)});
		jq.ajaxTargetId.load({self: this}, function(e) {			
			"use strict";
			var self = e.data.self,
				result = document.getElementById(self.arg.ajaxTargetId.substr(1)),
				frameData = null;
			if (result.contentWindow && result.contentWindow.document.body) {
				frameData = result.contentWindow.document.body;
			} else if (result.document && result.document.body) {
				frameData = result.document.body;
			} else if (result.contentDocument && result.contentDocument.body) {
				frameData = result.contentDocument.body;
			} else {
				throw new tiaacref.error('Bad return code from ajaxfile. Cannot access the iFrame.');
			}
			if (self.activeSubmit) {
				self.result = frameData.innerHTML;
				self.arg.onfinish(self.result);
				self.activeSubmit = false;
			}
		});
		jq.ajaxFormId.find('input[type="submit"]').first().click({self: this}, function(e) {
			var self = e.data.self;
			self.activeSubmit = true;
			self.arg.onstart();
			$(this).submit();
		});
	}
	

};
tiaacref.input.prototype.submit = function () {
	"use strict";
	if (this.arg.type !== "ajaxfile") {
		return;
	}
	this.activeSubmit = true;
	this.arg.onstart();
	this.jq.ajaxFormId.submit();
	return;
};

tiaacref.input.prototype.cancel = function () {
	"use strict";
	var result = document.getElementById(this.arg.ajaxTargetId.substr(1)),
		frameData = null;
	if (this.arg.type !== "ajaxfile") {
		return;
	}
	this.activeSubmit = false;
	if (result.contentWindow && result.contentWindow.document.body) {
		frameData = result.contentWindow.document.execCommand("Stop");
	} else if (result.document && result.document.body) {
		frameData = result.document.execCommand("Stop");;
	} else if (result.contentDocument && result.contentDocument.body) {
		frameData = result.contentDocument.execCommand("Stop");;
	} else {
		throw new tiaacref.error('Bad return code from ajaxfile. Cannot access the iFrame.');
	}
	
};
tiaacref.input.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('input.' + eventName) + this.arg.id;
};



/* ============================================= TCAPP ============================================== */

tiaacref.F2 = {
	libraryLocation: '',
	domainLocation: '',
	timeoutValue: 500,
	timeoutLimit: 3000,
	timeoutCounter: 0,
	F2iFrameInitialized: false,
	init: function() {
		if ((this.containers.containerIds.length === 0 && this.apps.appIds.length === 0) || this.libraryLocation === '' || this.domainLocation === '') {
			return;
		}
		if (typeof F2 === 'undefined') {
			this.timeoutCounter = this.timeoutLimit;
			$.ajax({
				url: this.libraryLocation,
				dataType: 'script',
				cache: true,
				success: this.waitForLibraryLoad()
			});
		} else {
			this.initF2();
		}
	},
	initF2: function() {
		var tcF2 = tiaacref.F2,
			containers = tiaacref.F2.containers,
			theContainer;
		if (!F2.isInit()) {
			F2.AppInstances = {};
			F2.init({
				xhr: {
					type: function (url, apps) {
						if (typeof containers.containerInstances[apps[0].customId] === 'undefined') {
							return tiaacref.defaults.TCAppContainer.type;
						}
						return containers.containerInstances[apps[0].customId].arg.type;
					},
					dataType: function (url, apps) {
						if (typeof containers.containerInstances[apps[0].customId] === 'undefined') {
							return tiaacref.defaults.TCAppContainer.dataType;
						}
						return containers.containerInstances[apps[0].customId].arg.dataType;
					}
				},
				beforeAppRender: function(app) {
					if (typeof containers.containerInstances[app.customId] === 'undefined') {
						return $('<section></section>');
					}
					theContainer = containers.containerInstances[app.customId];
					return $('<section class="' + theContainer.arg.cssClass + '"></section>')
						.appendTo(theContainer.arg.attachTo);
				},
				afterAppRender: function(app, html) {
					F2.AppInstances[app.name || app.Id] = app.instanceId;
					return $(app.root).append(html);
				}
			});
			if (containers.isRegistered === false) {
				F2.registerApps(containers.containerIds);
				tiaacref.F2.containers.isRegistered = true;
			}
		}
	},
	waitForLibraryLoad: function() {
		var tcF2 = tiaacref.F2;
		if (tcF2.timeoutCounter <= 0) {
			return;
		}
		tiaacref.F2.timeoutCounter -= tcF2.timeoutValue;
		if (typeof F2 === 'undefined') {
			setTimeout(tcF2.waitForLibraryLoad, tcF2.timeoutValue);
		} else {
			tcF2.initF2();
		}
	},
	setLibraryLocation: function(location) {
		if (typeof location === 'string') {
			this.libraryLocation = location;
		}
		return this.libraryLocation;
	},
	setDomainLocation: function(location) {
		if (typeof location === 'string') {
			this.domainLocation = location;
		}
		return this.domainLocation;
	},
	containers: {
		containerIds: [],
		containerInstances: {},
		isRegistered: false
	},
	apps: {
		appIds: [],
		isRegisterd: false
	}
};

tiaacref.TCAppContainer = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.TCAppContainer,
		i,
		containerArguments = {};
	if (typeof arg !== 'object') {
		arg = {
			description: defaults.description,
			cssClass: defaults.cssClass,
			context: defaults.context,
			params: defaults.params,
			type: defaults.type,
			dataType: defaults.dataType,
			id: tiaacref.createId(),
			enableBatchRequests: defaults.enableBatchRequests
		};
	} else {
		if (typeof arg.description !== 'string') {
			arg.description = defaults.description;
		}
		if (typeof arg.cssClass !== 'string') {
			arg.cssClass = defaults.description;
		}
		if (typeof arg.context !== 'object') {
			arg.context = defaults.context;
		}
		if (typeof arg.params !== 'object') {
			arg.params = defaults.params;
		}
		if (typeof arg.enableBatchRequests !== 'boolean') {
			arg.enableBatchRequests = defaults.enableBatchRequests;
		}
		if (typeof arg.type !== 'string') {
			arg.type = defaults.type;
		}
		if (typeof defaults.validType[arg.type] === 'undefined') {
			arg.type = defaults.type;
		}
		if (typeof arg.dataType !== 'string') {
			arg.dataType = defaults.dataType;
		}
		if (typeof arg.id !== 'string') {
			arg.id = tiaacref.createId();
		}
	}
	if (typeof arg.feed !== 'string') {
		throw new tiaacref.error('"feed" property in TCApp not specified or not a string.');
	}
	if (typeof arg.appId !== 'string') {
		throw new tiaacref.error('"appId" property in TCApp not specified or not a string.');
	}
	if (typeof arg.name !== 'string') {
		throw new tiaacref.error('"name" property in TCApp not specified or not a string.');
	}
	if (typeof arg.attachTo !== 'string') {
		throw new tiaacref.error('"attachTo" property in TCApp not specified or not a string.');
	}
	if (arg.description === "") {
		arg.description = arg.name;
	}
	if (tiaacref.F2.F2iFrameInitialized === false) {
		tiaacref.F2.F2iFrameInitialized = true;
//		$(arg.attachTo).append($('<iframe>').attr({'id': 'myF2iFrame'}));
	}
	arg.customId = arg.id;
	this.arg = arg;
	containerArguments = {
		appId: arg.appId,
		description: arg.description,
		name: arg.name,
		context: arg.context,
		manifestUrl: arg.feed,
		type: arg.type,
		dataType: arg.dataType,
		customId: arg.id,
		enableBatchRequests: arg.enableBatchRequests
	};
	for (i in arg.params) {
		containerArguments[i] = arg.params[i];
	}
	
	tiaacref.F2.containers.containerIds.push(containerArguments);
//	tiaacref.F2.containers.containerInstances[arg.appId] = this;
	tiaacref.F2.containers.containerInstances[arg.customId] = this;
};



/* ---------------------- megamenu selection --------------------------------- */
tiaacref.setMegaMenuSelected = function () {
	var regex = new RegExp("[\\?&]_pageLabel=([^&#]*)"),
		qs = regex.exec(window.location.href),
		menuOption,
		li;
	if (qs != null) {
		if (qs[1] !== 'fatwire_content_page') {
			menuOption = $('ul.l1nav a').removeClass('selected');
			menuOption = menuOption.filter('[href*="_pageLabel=' + qs[1] + '"]');
			if (menuOption.length !== 0) {
				menuOption.parents('li').each(function (index) {
					li = $(this);
					if (li.parent('ul.l1nav').length === 1) {
						li.children('a').addClass('selected');
						return false;
					}
				});
			}
		}
	}
};


tiaacref.setMenuAnalytics = function () {
	if (!tiaacref.useEnsighten) {
		$('.l1nav a').click(function (e) {
			$this = removeSpecialChar($(this).text());
			if($(this).parents(".default").length>0)
			{
				$parentEle = $.trim($(this).parents("li").find(" > a").text());
			} else {
				$parentEle = $.trim($(this).parents("li").parents("li").find(" > a").text());
			}
			$parentEle = removeSpecialChar($parentEle);
			$menuSiteName = s_omtr.channel;
			if(typeof($menuSiteName)!='undefined' && $menuSiteName != null && $menuSiteName != "")
			{
				$newLinkName = "Menu:" + $menuSiteName;
			} else {
				$newLinkName = "Menu:MyTC";
			}
			if ($parentEle) {
				$newLinkName += ":" + $parentEle;
			}
			$imgObject = $(this).find('img');
			$imgObjectAttr = $imgObject.attr('alt');
			$imgObjectAttr = removeSpecialChar($imgObjectAttr);
			if($imgObject.length>0)
			{
				if($imgObjectAttr)
				{
					$newLinkName += ":" + $imgObjectAttr;
				}
			} else {
				$newLinkName += ":" + $this;
			}
			s_omtr.prop34 = s_omtr.eVar34 = $newLinkName;
			s_omtr.tl(this, 'o', $newLinkName);
		});
	}
	function removeSpecialChar(ipString)
	{
		if(ipString!="" && ipString!=undefined){
//			var ipString2 = ipString.replace(/[^\w ]/gi, '');
//			return ipString2;
			return ipString.replace(/@/gi, '').replace(/,/gi, '');
		}
	}

};







/* ---------------------------------------------- mail -------------------------------------------- */
tiaacref.mail = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.mail,
		self = this;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style,
			cssClass: defaults.cssClass
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof arg.cssClass !== 'string') {
			arg.cssClass = defaults.cssClass;
		}
	}
	this.jq = {};
	this.arg = arg;
	this.messages = {};
	tcoipc.on(this.event('messageRegister'), function(payload) {
		self.messages[payload.name] = {read: false, message: payload.value};
	});
	return;
};
tiaacref.mail.prototype.initialize = function () {
	var defaults = tiaacref.defaults.mail,
		arg = this.arg,
		jq,
		self,
		messages,
		message,
		table,
		tbody,
		i,
		l,
		tr,
		previousMessage,
		nextMessage,
		currentMessage,
		tempMessage,
		element,
		messageVar,
		messageVarName,
		subject,
		messageDate,
		deletePayload = [],
		checkedMessages = {},
		td,
		rawId = arg.id.substring(1);
	jq = {
		id: $(arg.id),
		home: $('#mailhome' + rawId),
		messages: $('#mailmessages' + rawId),
		trTemplate: $('#mailrow' + rawId + '-').clone(true),
		mailPopup: $('#mailPopup' + rawId),
		deleteChecked: $('#deleteChecked' + rawId),
		mailTable: $('#mailtable' + rawId),
		selectAll: $('#selectall' + rawId),
		noMail: $('#mailNoMail' + rawId)
	};
	if (jq.id.length === 0) {
		throw new tiaacref.missingDOM("Missing DOM element named '" + arg.id + "'.");
	}
	this.jq = jq;
	this.deletePayload = [];
	self = this;
	this.popup =  new tiaacref.popup({
		url: '',
		id: '',
		popup: '#mailPopup' + arg.id.substring(1),
		title: 'Delete Message',
		size: 'small',
		detached: true
	});
	this.popup.jq.popup.find('button[data-mail="no"]').off('click.mail.cl').on('click.mail.cl', function(event) {
		event.stopPropagation();
		self.popup.jq.popup.attr({'data-mail': 'no'});
		self.popup.close();
	});
	this.popup.jq.popup.find('button[data-mail="yes"]').off('click.mail.cl').on('click.mail.cl', function(event) {
		event.stopPropagation();
		self.popup.jq.popup.attr({'data-mail': 'yes'});
		self.popup.close();
	});
	tcoipc.on(this.popup.event('open'), function(payload) {
		self.popup.jq.popup.attr({'data-mail': 'no'});
	});
	tcoipc.on(this.popup.event('close'), function(payload) {
		var i,
			l;
		if (deletePayload != []) {
			l = deletePayload.length;
			for (i = 0; i < l ; i ++) {
				tcoipc.fire(deletePayload[i].message.event('setDelete'), (self.popup.jq.popup.attr('data-mail') === 'yes' ? true : false));
			}
		}
		if (self.popup.jq.popup.attr('data-mail') === 'yes' && deletePayload != []) {
			l = deletePayload.length;
			for (i = 0; i < l; i ++) {
				tiaacref.hideElement(jq.home.find('tr[data-tag="' + deletePayload[i].message.arg.id + '"]'));
				tcoipc.fire(self.event('messageDeleted'), {message: deletePayload[i].message});
		}
			showHome();
		}
		deletePayload = [];
	});
	// check for no messages
	messages = jq.messages.children('.view-message');
	l = messages.length;
	if (l == 0) {
		tiaacref.hideElement(jq.home);
		tiaacref.showElement(jq.noMail);
		return; 
	} else {
		tiaacref.showElement(jq.home);
		tiaacref.hideElement(jq.noMail);
	}
	jq.deleteChecked.off('click.mail.cl').on('click.mail.cl', function(event) {
		event.stopPropagation();
		event.preventDefault();
		deletePayload = [];
		tcoipc.fire(self.event('deleteSelected'), {messages: checkedMessages});
		for (i in checkedMessages) {
			if (checkedMessages[i] != null) {
				deletePayload.push({message: checkedMessages[i], element: null});
			}
		}
		if (deletePayload.length !== 0) {
			self.popup.jq.popup.find('[data-mail="message"]').html("Are you sure you want to delete the checked messages?");
			self.popup.open();
		} else {
			deletePayload = [];
		}
	});

	
	
	
	tcoipc.fire(self.event('numberMessages'), {numberMessages: l});
	table = jq.mailTable;
	tbody = table.children('tbody');
	tbody.empty();
	previousMessage = '';
	for (i = 0; i < l; i ++) {
		message = $(messages[i]);
		messageVarName = message.attr('data-mailvar');
		messageVar = this.messages[messageVarName].message;
		currentMessage = message.attr('id');
		tr = jq.trTemplate.clone(true);
		tr.removeClass('hidden');
		td = tr.children('td[data-mail="checkbox"]');
		td.attr({
			id: td.attr('id') + message.attr('id'),
			name: td.attr('name') + message.attr('name')
		});
		subject = message.find('[data-mail="subject"]').html();
		if (subject.indexOf('SUBJECT: ') === 0) {
			subject = subject.substring(9);
		}
		td = tr.children('td[data-mail="subject"]');
		td.attr({
			'data-sortvalue': subject
		});
		td.children('a').attr({href: '#' + message.attr('id')}).html(subject);
		element = tr.children('td[data-mail="checkbox"]').find('input');
		element.attr({id: element.attr('id') + message.attr('id')});
		element.attr({name: element.attr('name') + message.attr('id')});
		element.off('change.mail.cl').on('change.mail.cl', function(event) {
			var messageVar = self.messages[$(this).closest('tr').attr('data-mailvar')].message;
			event.stopPropagation();
			if (this.checked) {
				checkedMessages[messageVar.arg.id.substring(1)] = messageVar;
			} else {
				checkedMessages[messageVar.arg.id.substring(1)] = null;
				tcoipc.fire(self.event('clearSelectAll'), {});
			}
			tcoipc.fire(self.event('selectMessage'), {message: messageVar, state: this.checked});
		});
		td = tr.children('[data-mail="date"]');
		messageDate = message.find('[data-mail="date"]').text(); 
		td.text(messageDate).attr({
			'data-sortvalue': messageDate
		});
		tr.attr({
			'data-tag': '#' + message.attr('id'),
			id: tr.attr('id') + message.attr('id'),
			'data-mailvar': messageVarName
			});
		if (message.attr('data-mailread') === 'false') {
			tr.addClass('txtb');
			this.messages[message.attr('data-mailvar')].read = false;
		} else {
			tr.removeClass('txtb');
			this.messages[message.attr('data-mailvar')].read = true;
		}
		tbody.append(tr);
		//
		// Process the delete button
		//
		tcoipc.on(messageVar.event('delete'), function(payload) {
			deletePayload = [payload];
			self.popup.jq.popup.find('[data-mail="message"]').html("Are you sure you want to delete this message?");
			self.popup.open();
		});
		//
		// Process the Next / Previous Buttons
		//
		tcoipc.on(messageVar.event('next'), function(payload) {
			element = findNext(payload.message.jq.id);
			if (element.length === 0) {
				showHome();
			} else {
				showMessage(self.messages[element.attr('data-mailvar')].message);
			}
		});
		tcoipc.on(messageVar.event('previous'), function(payload) {
			element = findPrevious(payload.message.jq.id);
			if (element.length === 0) {
				showHome();
			} else {
				showMessage(self.messages[element.attr('data-mailvar')].message);
			}
		});
		//
		// Process Home
		//
		tcoipc.on(messageVar.event('home'), function(payload) {
			showHome();
		});
	}
	tcoipc.fire(self.event('numberUnreadMessages'), {numberUnreadMessages: jq.messages.children('div[data-mailread="false"]').length});
	// Tablesort
	this.tableSort = new tiaacref.tableSort({
		id: '#mailtable' + arg.id.substring(1),
		multiSort: false,
		caseSensitive: false,
		initialSort: ''
	});
	tcoipc.fire(self.event('messageCurrent'), {message: null});
	jq.selectAll.off('change.mail.cl').on('change.mail.cl', function(event) {
		event.stopPropagation();
		var checked = this.checked,
			state = checked;
		jq.home.find('table tbody tr input:checkbox').prop('checked', checked).trigger('change');
		tcoipc.fire(self.event('selectAll'), {state: this.checked});
	});
	tbody.find('tr td a:[data-mail="messageLink"]').off('click.mail.cl').on('click.mail.cl', function(event) {
		event.preventDefault();
		var message = self.messages[$($(this).attr('href')).attr('data-mailvar')].message;
		tcoipc.fire(self.event('messageClick'), {message: message, element: this});
		showMessage(message);
	});
	tcoipc.on(self.event('messageRead'), function(payload) {
		self.messages[$(payload.message.arg.id).attr('data-mailvar')].read = true;
		jq.home.find('tr[data-tag="' + payload.message.arg.id + '"]').removeClass('txtb');
	});
	tcoipc.on(self.event('clearSelectAll'), function(payload) {
		jq.selectAll.prop('checked', false);
	});
	
	function findPrevious(element) {
		return element.prevAll('div.view-message').not('[data-deleted="true"]').first();
	}
	function findNext(element) {
		return element.nextAll('div.view-message').not('[data-deleted="true"]').first();
	}
	function fixNextPreviousButtons(message) {
		if (findPrevious(message.jq.id).length === 0) {
			tiaacref.hideElement(message.jq.previous);
		} else {
			tiaacref.showElement(message.jq.previous);
		}
		if (findNext(message.jq.id).length === 0) {
			tiaacref.hideElement(message.jq.next);
		} else {
			tiaacref.showElement(message.jq.next);
		}
	}
	function showMessage(message) {
		fixNextPreviousButtons(message);
		tiaacref.hideElement(jq.messages.children('.view-message'));
		tiaacref.hideElement(jq.home);
		tiaacref.showElement(message.jq.id);
		tcoipc.fire(self.event('messageCurrent'), {message: message});
		tcoipc.fire(self.event('messageRead'), {message: message});
		tcoipc.fire(message.event('setRead'), true);
	}
	function showHome () {
		tiaacref.hideElement(jq.messages.children('.view-message'));
		var numberMessages = jq.mailTable.children('tbody').children('tr').not('.hidden').length,
			numberUnreadMessages = jq.messages.children('div[data-mailread="false"]').length;
		if (numberMessages === 0) {
			tiaacref.hideElement(jq.home);
			tiaacref.showElement(jq.noMail);
		} else {
		tiaacref.showElement(jq.home);
			tiaacref.hideElement(jq.noMail);
	}
		tcoipc.fire(self.event('numberMessages'), {numberMessages: numberMessages});
		tcoipc.fire(self.event('numberUnreadMessages'), {numberUnreadMessages: numberUnreadMessages});
		tcoipc.fire(self.event('messageCurrent'), {message: null});
	}
	
};
tiaacref.mail.prototype.event = function (eventName) {
	"use strict";
	return tiaacref.eventName('mail.' + eventName) + this.arg.id;
};

tiaacref.mail.prototype.getNumberMessages = function () {
	return this.jq.messages.children('div.view-message').length;
};

tiaacref.mail.prototype.getNumberUnreadMessages = function () {
	return this.jq.messages.children('div[data-mailread="false"]').length;
};

/* ---------------------------------------------- mailMessage -------------------------------------------- */
tiaacref.mailMessage = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.mailMessage,
		jq,
		self;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style,
			cssClass: defaults.cssClass
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (typeof arg.cssClass !== 'string') {
			arg.cssClass = defaults.cssClass;
		}
	}
	jq = {
		id: $(arg.id),
		subject: null,
		home: null,
		next: null,
		previous: null,
		'delete': null,
		archive: null
	};
	if (jq.id.length === 0) {
		throw new tiaacref.missingDOM("Missing DOM element named '" + arg.id + "'.");
	}
	jq.subject = jq.id.find('[data-mail="subject"]');
	jq.home = jq.id.find('[data-mail="home"]');
	jq.next = jq.id.find('[data-mail="next"]');
	jq.previous = jq.id.find('[data-mail="prev"]');
	jq['delete'] = jq.id.find('[data-mail="delete"]');
	jq.archive = jq.id.find('[data-mail="archive"]');
	this.deleted = false;
	this.archived = false;
	this.read = (jq.id.attr("data-mailread") == "true" ? true : false),
	this.jq = jq;
	this.arg = arg;
	self = this;
	if (jq.subject.length > 0) {
		jq.subject.html('SUBJECT: ' + jq.subject.html());
	}
	jq.home.on('click', function(event) {
		event.preventDefault();
		tcoipc.fire(self.event('home'), {message: self, element: this});
	});
	jq.next.on('click', function(event) {
		event.preventDefault();
		tcoipc.fire(self.event('next'), {message: self, element: this});
	});
	jq.previous.on('click', function(event) {
		event.preventDefault();
		tcoipc.fire(self.event('previous'), {message: self, element: this});
	});
	jq['delete'].on('click', function(event) {
		event.preventDefault();
		tcoipc.fire(self.event('setDelete'), true);
		tcoipc.fire(self.event('delete'), {message: self, element: this});
	});
	jq.archive.on('click', function(event) {
		event.preventDefault();
		tcoipc.fire(self.event('setArchive'), true);
		tcoipc.fire(self.event('archive'), {message: self, element: this});
	});
	tcoipc.on(self.event('setArchive'), function(payload) {
		if (typeof payload !== 'boolean') {
			payload = true;
		}
		self.archived = payload;
		self.jq.id.attr({'data-archived': payload});
	});
	tcoipc.on(self.event('setDelete'), function(payload) {
		if (typeof payload !== 'boolean') {
			payload = false;
		}
		self.deleted = payload;
		self.jq.id.attr({'data-deleted': payload});
	});
	tcoipc.on(self.event('setRead'), function(payload) {
		if (typeof payload !== 'boolean') {
			payload = false;
		}
		self.read = payload;
		self.jq.id.attr({'data-mailread': payload});
	});
	return;
};
tiaacref.mailMessage.prototype = {
	event: function (eventName) {
		return tiaacref.eventName('mailMessage.' + eventName) + this.arg.id;
	},
	'delete': function () {
		tcoipc.fire(this.eventName('setDelete'), true);
	},
	isDeleted: function () {
		return this.deleted;
	},
	isArchived: function () {
		return this.archived;
	},
	disableHome: function () {
		tiaacref.hideElement(this.jq.home);
	},
	enableHome: function () {
		tiaacref.showElement(this.jq.home);
	},
	disablePrevious: function () {
		tiaacref.hideElement(this.jq.previous);
	},
	enablePrevious: function () {
		tiaacref.showElement(this.jq.previous);
	},
	disableNext: function () {
		tiaacref.hideElement(this.jq.next);
	},
	enableNext: function () {
		tiaacref.showElement(this.jq.next);
	},
	disableDelete: function () {
		tiaacref.hideElement(this.jq['delete']);
	},
	enableDelete: function () {
		tiaacref.showElement(this.jq['delete']);
	},
	disableArchive: function () {
		tiaacref.hideElement(this.jq.archive);
	},
	enableArchive: function () {
		tiaacref.showElement(this.jq.archive);
	},
	getTag: function () {
		return this.jq.id.attr('data-mailtag');
	},
	getAnalytics: function () {
		return this.arg.analytics;
	}
};

/* ---------------------------------------------- fileDoc -------------------------------------------- */
tiaacref.FileDoc = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.FileDoc,
		self = this;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
	}
	this.jq = {
			id:$(arg.id),
			labeltext:$("#labeltext"+arg.id.substr(1)),
			checkbox:$("#inputChk"+arg.id.substr(1)),
			icon: $('#movableicon' +arg.id.substr(1))
	};
	this.arg = arg;
	this.isSelected = false;
	this.jq.checkbox.off("click.button").on("click.button",function(event){
		self.select($(this).prop("checked"));
	}); 
	tcoipc.on(this.event("select"), function(flag){
		this.select(flag);
	});
	this.jq.labeltext.off("click.labeltext").on("click.labeltext", function(event){
		event.preventDefault();
		self.arg.unread=false;
		self.jq.id.removeClass("unread");
	});
	
	this.arg.draggableElement = $('<div class="dragging">').css('opacity', 0.8).text(this.jq.labeltext.text());
	this.arg.draggableElement.data('documents', this);
	this.jq.icon.draggable({
	revert: true,
	revertDuration: 50,
	helper: function () {
		/*var documents = [],
			allDocuments;
		if (self.arg.parent.selectedDocuments > 1 && self.document.isSelected) {
			self.arg.draggableElement.text('Moving ' + self.arg.parent.selectedDocuments + ' items');
			allDocuments = self.arg.parent.documents;
			for (var i in allDocuments) {
				if (allDocuments[i].document.isSelected) {
					documents.push(allDocuments[i]);
				}
			}
		} else {
			documents.push(self);
			self.arg.draggableElement.text(self.jq.messageSubject.text());
		}*/
		self.arg.draggableElement.text(self.jq.labeltext.text());
		self.arg.draggableElement.data('documents', self);
		return self.arg.draggableElement; 
	}
	});

	return;
};

tiaacref.FileDoc.prototype={
		remove: function(){
			if (this.isSelected){
				tcoipc.fire(this.arg.parent.event("docDeleted"), this);
				this.jq.id.remove()
			}
		},
		event: function (eventName) {
			return tiaacref.eventName('FileDoc.' + eventName) + this.arg.id;
		},
		move: function (fileFolder) {
			if(this.isSelected){
				tcoipc.fire(this.arg.parent.event("docMove"), {document:this, toFolder:fileFolder});
				fileFolder.jq.table.append(this.jq.id);
				this.arg.originalParent=this.arg.parent;
				this.arg.parent=fileFolder;
			}
		},
		setLabel: function (labelValue) {
			this.arg.label = labelValue;
			this.jq.labeltext.text(labelValue);
			tcoipc.fire(this.arg.parent.event("docLabel"), this);
		},
		select: function(flag) {
			this.isSelected=flag;
			this.jq.checkbox.prop("checked",flag);
			if (this.isSelected){
				this.jq.id.addClass("rowHighlight").attr("aria-selected",true);	
			}else{
				this.jq.id.removeClass("rowHighlight").removeAttr("aria-selected");	
			}
			tcoipc.fire(this.arg.parent.event("selectedDoc"),this);
			tcoipc.fire(this.arg.parent.event("countselecteddocs"),null);
		},
		editMe: function() {
			var text = this.jq.labeltext.text();
				$("#editFileLabelInput").val(text);
				$("#editfilelabel").text("Label Name:");
				editfolderpop.jq.popupDialog.find(".ui-dialog-title").text("Edit File");
				$("#editbutton span").text("Update File");
				editfolderpop.arg.document=this;
				editfoldererrorjsvar.hide();
				editfolderpop.open();
			return false;
		}
	};

/* ---------------------------------------------- fileFolder -------------------------------------------- */
tiaacref.FileFolder = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.FileFolder,
		self = this;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
	}
	this.jq = {
			id:$(arg.id),
			table:$("#table"+arg.id.substr(1)),
			folderName:$("#foldername"+arg.id.substr(1)),
			openIcon:$("#openIcon"+arg.id.substr(1)),
			closeIcon:$("#closeIcon"+arg.id.substr(1)),
			trashIcon:$("#trashIcon"+arg.id.substr(1)),
			checkboxSelectAll:$("#filesSelectAll"+arg.id.substr(1))
	};
	this.arg = arg;
	this.jq.folderName.on("click.filefolder",function(event){
		event.preventDefault();
		tcoipc.fire(self.arg.parent.event("folderSelected"),{folder:self});
		self.showfolder();
		self.countSelectedDocs();
	});
	this.jq.editIcon = self.jq.id.children("a.editLink");
	this.docs = {};
	this.numdocs = 0;
	tcoipc.on(this.event("docRegister"),function(payload){
		self.docs[payload.variable.arg.id]=payload.variable;
		self.docs[payload.variable.arg.id].arg.parent=self;
		self.numdocs++;
	});
	tcoipc.on(this.event("countselecteddocs"),function(payload){
		self.countSelectedDocs();
	});
	tcoipc.on(this.event("selectedDoc"),function(payload){
		tcoipc.fire(self.arg.parent.event("selectedDoc"),{folder:self, document:payload});
	});
	tcoipc.on(this.event("docDeleted"),function(payload){
		tcoipc.fire(self.arg.parent.event("docDeleted"),{folder:self, document:payload});
	});
	tcoipc.on(this.event("docMove"),function(payload){
		tcoipc.fire(self.arg.parent.event("docMove"),{fromFolder:self, document:payload.document, toFolder:payload.toFolder});
	});
	tcoipc.on(this.event("docLabel"),function(payload){
		tcoipc.fire(self.arg.parent.event("docLabel"),{folder:self, document:payload});
	});
	this.tablesort = new tiaacref.tableSort({
		id: '#table' + arg.id.substr(1),
		multiSort: false,
		caseSensitive: true
		});
	tcoipc.on(this.tablesort.event("afterSort"),function(){
		var shading="odd";
		self.jq.table.find("tbody tr").not(".noFilesMessage").each(function(index,element){
			$(element).removeClass("odd even").addClass(shading);
			if(shading == "odd"){
				shading = "even";	
			}
			else {
				shading = "odd";
			}
		});
	});
	this.jq.checkboxSelectAll.off("click.selectall").on("click.selectall",function(event){
		var selected=$(this).prop("checked");
			var y;
			for (y in self.docs){
				self.docs[y].select(selected);
			}	
			tcoipc.fire(self.event("countselecteddocs"),null);
	});
	this.jq.id.droppable({
			over: function (event, ui) {
				self.jq.id.addClass('dropping');
			},
			out: function (event, ui) {
				self.jq.id.removeClass('dropping');
			},
			drop: function (event, ui) {
				if (ui.helper) {
					if (self.arg.parent) {
						self.arg.parent.processPopup("drop",{folder:self, document:ui.helper.data('documents')});
					}
				}
				self.jq.id.removeClass('dropping');
			}
		});
	return;
};

tiaacref.FileFolder.prototype={
		showfolder: function(){
			this.jq.table.parent("div").children("table").addClass("hidden");
			var i;
			for(i in this.docs){
				this.docs[i].select(false);
			}
			this.jq.table.removeClass("hidden");
			this.jq.id.parent("ul").children('li').children("a.folderName").removeClass("active").children("img").addClass("hidden").filter(".closedFolder, .trashFolder").removeClass("hidden");
			this.jq.openIcon.removeClass("hidden");
			this.jq.closeIcon.addClass("hidden");
			this.jq.folderName.addClass("active");
			if(this.numdocs==0){
				this.jq.table.find("tbody tr.noFilesMessage").removeClass("hidden");
				this.jq.checkboxSelectAll.prop("checked", false);
			}
			else {
				this.jq.table.find("tbody tr.noFilesMessage").addClass("hidden");
			}
		},
		event: function (eventName) {
			return tiaacref.eventName('FileFolder.' + eventName) + this.arg.id;
		},
		countSelectedDocs: function () {
			var ind=0;
			var count=0;
			for(ind in this.docs){
				if(this.docs[ind].isSelected){
					count++;
				}
			}
			if (this.numdocs == count && this.numdocs!=0){
				this.jq.checkboxSelectAll.prop("checked", true);
			}	
			else{
				this.jq.checkboxSelectAll.prop("checked", false);
			}
			tcoipc.fire(this.arg.parent.event("docSelected"),{count:count, numdocs:this.numdocs});
		},
		renameMe: function() {
			var text = this.arg.folderName;
				$("#editFileLabelInput").val(text);
				$("#editfilelabel").text("Folder Name:");
				editfolderpop.jq.popupDialog.find(".ui-dialog-title").text("Rename Folder");
				$("#editbutton span").text("Rename Folder");
				editfolderpop.arg.document=this;
				editfoldererrorjsvar.hide();
				editfolderpop.open();
			return false;
		},
		setLabel: function (labelValue) {
			this.arg.folderName = labelValue;
			this.jq.folderName.children("span").text(labelValue);
			this.arg.parent.showActiveFolder();
			tcoipc.fire(this.arg.parent.event("folderLabel"), this);
		}
};

/* ---------------------------------------------- fileManager -------------------------------------------- */
tiaacref.FileManager = function (arg) {
	"use strict";
	var defaults = tiaacref.defaults.FileManager,
		self = this,
		i,
		id = arg.id.substr(1);
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
	}
	this.jq = {
			id:$(arg.id),
			checkbox:$("#selectall"+id),
			foldername:$("#folderNameHeader"+id),
			tablescontainer:$("#tablescontainer"+id),
			folderList:$("#folderList"+id),
			selectfile:$("#selected-documents-list"+id),
			selecteddocsection:$("#selected-documents-section"+id),
			selecteddocuments:$("#selected-documents"+id)
	};
	this.arg = arg;
	this.folders = {};
	this.folderselected = null;
	
	tcoipc.on(this.event("folderRegister"),function(payload){
		self.folders[payload.variable.arg.id]=payload.variable;
		if(payload.variable.arg.active){
			self.jq.foldername.text(payload.variable.arg.folderName);
			self.folderselected = payload.variable.arg.id;
			}
	});
	tcoipc.on(this.event("docSelected"),function(payload){
		if (payload.count>0) {
			self.arg.buttons.move.enable();
			self.arg.buttons.trash.enable();
			self.arg.buttons.restore.enable();
			self.arg.buttons.download.enable();
			self.arg.buttons.trdownload.enable();
			self.arg.buttons.unread.enable();
			self.arg.buttons.permDeleteButton.enable();
		} else {
			self.arg.buttons.move.disable();
			self.arg.buttons.trash.disable();
			self.arg.buttons.restore.disable();
			self.arg.buttons.download.disable();
			self.arg.buttons.trdownload.disable();
			self.arg.buttons.unread.disable();
			self.arg.buttons.permDeleteButton.disable();
		}
	});

	tcoipc.on(this.event("folderSelected"),function(payload){
		self.folderselected=payload.folder.arg.id;
		self.jq.checkbox.prop("checked", false);
		self.jq.foldername.text(payload.folder.arg.folderName + 
				(payload.folder.numdocs>0?" (" + payload.folder.numdocs + ")":""));
		if(payload.folder.arg.folderName=="Trash") {
			$("#default-actions-toolbar"+arg.id.substr(1)).addClass("hidden");
			$("#files-trash-actions-toolbar"+arg.id.substr(1)).removeClass("hidden");
		} else {
			$("#files-trash-actions-toolbar"+arg.id.substr(1)).addClass("hidden");
			$("#default-actions-toolbar"+arg.id.substr(1)).removeClass("hidden");
		}
	});
	
	
	

	this.jq.selectfile.off('change.getfile').on('change.getfile',function(e){
		
		uploadmessagesuccessjsvar.jq.id.find('.messageBodyContent ul').empty();
		uploadmessagesuccessjsvar.close();
		uploadmessagesuccesscancelledjsvar.close();
		uploadmessageerrorjsvar.close();
		//var rowtemplate = '<tr><td scope="row" class="txtc">(!!FILEEXT!!)</td><td style="word-wrap: break-word; max-width:185px !important;"><span class="file-name">!!FILEROW!!</span></td><td><div class="input"><input type="text" value="!!FILENAME!!" placeholder="Label Name" class="clear inputm" id="new_file_label!!UNIQUEID!!" name="new_file_label!!UNIQUEID!!"  maxlength="30" /></div></td><td class="txtc"><button class="closeLink pts prxs remove-file-from-list" style="background:transparent" role="button" title="Remove"><span class="icon"></span></button></td></tr>';
		//var errorrowtemplate = '<tr><td scope="row" class="txtc">(!!FILEEXT!!)</td><td style="word-wrap: break-word; max-width:185px !important;" ><span class="file-name">!!FILEROW!!</span></td><td class="mbm alertHighlight"><input type="hidden" value="filetype" id="new_file_label!!UNIQUEID!!" name="new_file_label!!UNIQUEID!!"  maxlength="30" />!!LABEL!!</td><td><button class="closeLink pts prxs remove-file-from-list" style="background:transparent" role="button" aria-disabled="false" title="Remove"><span class="icon"></span></button></td></tr>';
		
		var rowtemplate = '<tr><td scope="row" class="txtc">(!!FILEEXT!!)</td><td style="word-wrap: break-word; max-width:185px !important;"><span class="file-name">!!FILEROW!!</span></td><td !!CLASSALERT!!>!!LABELTEXT!!</td><td class="txtc"><button class="closeLink pts prxs remove-file-from-list" style="background:transparent" role="button" !!AREADISABLED!! title="Remove"><span class="icon"></span></button></td></tr>';
		
		var selectedDocuments = (tiaacref.isIE().ie) ? e.target.value.substring(e.target.value.lastIndexOf('\\') + 1) : e.currentTarget.files;
		var selectedDocumentsArray = $.makeArray(selectedDocuments);

		if (selectedDocumentsArray.length > 10) {
			//uploadFileButton.disable();
			uploadmessageerrorjsvar.addMessage("The maximum number of files (10) that can be uploaded has been exceeded. Please remove ("+(Math.max(selectedDocumentsArray.length-10,uploadfilepopjsvar.arg.numselecteddocs))+") files before proceeding.","listing");
			uploadmessageerrorjsvar.open();
		} else {
			$(selectedDocumentsArray).each(function(i){
				var acceptableFileTypes = ['doc','docx','xls','xlsx','jpeg','jpg','pdf','iff','tif','tiff','bmp','png','pjg','pcx','ppt','pptx'];
				var fileNameRaw = (tiaacref.isIE().ie) ? selectedDocuments : selectedDocuments.item(i).name
				, fileName = fileNameRaw.substr(0, fileNameRaw.lastIndexOf('.'))
				, fileSize = (tiaacref.isIE().ie) ? 30000 : selectedDocuments.item(i).size
				, fileNameSplitArray = fileNameRaw.split('.')
				, fileExtension = fileNameSplitArray[fileNameSplitArray.length-1].toLowerCase()
				, uniqueId = Math.round(new Date().getTime() + (Math.random() * 100))
				, labelLargeFile = '<input type="hidden" value="filetype" id="new_file_label'+uniqueId+'" name="new_file_label'+uniqueId+'"  maxlength="30" />This file size should be less than 3 MB.'
				, labelInvalidFormat = '<input type="hidden" value="filetype" id="new_file_label'+uniqueId+'" name="new_file_label'+uniqueId+'"  maxlength="30" />This file has an invalid file format.'
				, classalert = 'class="mbm alertHighlight"'
				, labelField = '<div class="input"><input type="text" value='+fileName+' placeholder="Label Name" class="clear inputm" id="new_file_label'+uniqueId+'" name="new_file_label'+uniqueId+'"  maxlength="30" /></div>'
				, areadisabled = 'aria-disabled="false"'; 
				uploadfilepopjsvar.arg.numselecteddocs ++;
				
				if(jQuery.inArray(fileExtension,acceptableFileTypes)!=-1) {
					if(fileSize <= 3*1048576) {
						var td = rowtemplate.replace(/!!FILEEXT!!/gi,fileExtension.toUpperCase()).replace(/!!FILEROW!!/gi,fileNameRaw).replace(/!!FILENAME!!/gi,fileName).replace(/!!UNIQUEID!!/gi,uniqueId).replace(/!!LABELTEXT!!/gi,labelField).replace(/!!AREADISABLED!!/gi,"").replace(/!!CLASSALERT!!/gi,"");
					} else	{
						uploadfilepopjsvar.arg.invalidsize++;
						var td = rowtemplate.replace(/!!FILEEXT!!/gi,fileExtension.toUpperCase()).replace(/!!FILEROW!!/gi,fileNameRaw).replace(/!!FILENAME!!/gi,fileName).replace(/!!UNIQUEID!!/gi,uniqueId).replace(/!!LABELTEXT!!/gi,labelLargeFile).replace(/!!CLASSALERT!!/gi,classalert).replace(/!!AREADISABLED!!/gi,areadisabled);
						td = td.replace("alertHighlight", "alertHighlight errorFileSize")
						//$('<tr><td scope="row" class="txtc">('+fileExtension.toUpperCase()+')</td><td class="mbm alertHighlight" style="word-wrap: break-word; max-width:185px !important;"><span class="file-name">'+fileNameRaw+'</span></td><td class="mbm alertHighlight"><input type="hidden" value="filesize" id="new_file_label'+uniqueId+'" name="new_file_label'+uniqueId+'"  maxlength="30" />This file size should be less than 3 MB. </td><td><button class="closeLink pts prxs remove-file-from-list" style="background:transparent" role="button" aria-disabled="false" title="Remove"><span class="icon"></span></button></td></tr>').appendTo('#selected-documents');	   
					}
				} else {
					uploadfilepopjsvar.arg.invalidtype++;
					var td = rowtemplate.replace(/!!FILEEXT!!/gi,fileExtension.toUpperCase()).replace(/!!FILEROW!!/gi,fileNameRaw).replace(/!!FILENAME!!/gi,fileName).replace(/!!UNIQUEID!!/gi,uniqueId).replace(/!!LABELTEXT!!/gi,labelInvalidFormat).replace(/!!CLASSALERT!!/gi,classalert).replace(/!!AREADISABLED!!/gi,areadisabled);
					td = td.replace("alertHighlight", "alertHighlight errorInvalidType")
					//$('<tr><td scope="row" class="txtc">('+fileExtension.toUpperCase()+')</td><td class="mbm alertHighlight" style="word-wrap: break-word; max-width:185px !important;"><span class="file-name">'+fileNameRaw+'</span></td><td class="mbm alertHighlight"><input type="hidden" value="filetype" id="new_file_label'+uniqueId+'" name="new_file_label'+uniqueId+'"  maxlength="30" />This file has an invalid file format.</td><td><button class="closeLink pts prxs remove-file-from-list" style="background:transparent" role="button" aria-disabled="false" title="Remove"><span class="icon"></span></button></td></tr>').appendTo('#selected-documents');							
				}
				var appendtd = $(td);
				appendtd.find("button.remove-file-from-list").on("click.filemanager",function(event){
					var trHandle=$(this).parents("tr:first");
					if (trHandle.find(".errorFileSize").length > 0){
						uploadfilepopjsvar.arg.invalidsize--;
					}
					if (trHandle.find(".errorInvalidType").length > 0){
						uploadfilepopjsvar.arg.invalidtype--;
					}
					trHandle.remove();
					uploadfilepopjsvar.arg.numselecteddocs --;
					
				});
				
				self.jq.selecteddocuments.append(appendtd);
			}); // each	
			
		tiaacref.showElement(self.jq.selecteddocsection);	
		
		//if($('#selected-documents .alertHighlight').size()>0) {
		if(uploadfilepopjsvar.arg.invalidsize + uploadfilepopjsvar.arg.invalidtype + uploadfilepopjsvar.arg.duplicatelabel + uploadfilepopjsvar.arg.nolabel > 0) {
			uploadmessageerrorjsvar.jq.id.find('.messageBodyContent ul').empty();
			//$('#file-upload-error').find('ul').empty();


				if (uploadfilepopjsvar.arg.invalidsize > 0){
					uploadmessageerrorjsvar.addMessage("The file(s) listed in red exceed the maximum file size and can�t be uploaded. Please remove these files before proceeding.","listing");
							//$('#file-upload-error').find('ul').append(($('#file-upload-error:icontains(size)').length > 0) ? '' : '<li>The file(s) listed in red exceed the maximum file size and can�t be uploaded. Please remove these files before proceeding.</li>');
				}

				if(uploadfilepopjsvar.arg.invalidtype > 0){
					uploadmessageerrorjsvar.addMessage("The file type you chose to upload is unsupported. Please choose another file type.","listing");
						//	$('#file-upload-error').find('ul').append(($('#file-upload-error:icontains(invalid)').length > 0) ? '' : '<li>The file type you chose to upload is unsupported. Please choose another file type.</li>');
				} 


				if(uploadfilepopjsvar.arg.duplicatelabel > 0){
					uploadmessageerrorjsvar.addMessage("The file(s) listed below contain duplicate labels within this folder.  Please choose another label.","listing");
  						//$('#file-upload-error').find('ul').append(($('#file-upload-error:icontains(duplicate)').length > 0) ? '' : '<li>The file(s) listed below contain duplicate labels within this folder.  Please choose another label.</li>');
				} 
				
				if(uploadfilepopjsvar.arg.nolabel > 0){
					uploadmessageerrorjsvar.addMessage("The file(s) listed below are missing a label. Please choose a label.","listing");
  						//$('#file-upload-error').find('ul').append(($('#file-upload-error:icontains(missing)').length > 0) ? '' : '<li>The file(s) listed below are missing a label. Please choose a label.</li>');
				} 
				if(uploadfilepopjsvar.arg.numselecteddocs > 10){
					uploadmessageerrorjsvar.addMessage("The maximum number of files (10) that can be uploaded has been exceeded. Please remove ("+(uploadfilepopjsvar.arg.numselecteddocs - 10)+") files before proceeding.","listing");
				}		

				uploadmessageerrorjsvar.open();

		} else {
			uploadmessageerrorjsvar.close();
		}

	}
		
	});
	
	
	
	return;
};

tiaacref.FileManager.prototype={
		event: function (eventName) {
			return tiaacref.eventName('FileManager.' + eventName) + this.arg.id;
		},
		showActiveFolder: function () {
			if (this.folderselected!=null){
				this.folders[this.folderselected].showfolder();
			}
		},
		folderfill: function(element,currentfolder) {
			var index;
			element.empty();
			element.append("<option value=\"NONE\" selected=\"selected\">- Select -</option>");
			for(index in this.folders){
				if((index!=this.folderselected || currentfolder===false) && this.folders[index].arg.type != "trash"){
					
					element.append("<option value=\""+index+"\"> "+this.folders[index].arg.folderName+" </option>")
				}	
			}
		},
		preparePopup: function(type) {
			if (type == "move") {
				this.folderfill($('#moverestoreSelect'+ this.arg.id.substr(1)),true);
				moverestorepopjsvar.arg.dialogType=type;
				moverestorepopjsvar.jq.popupDialog.find(".ui-dialog-title").text("Move File(s)");
				moverestorepopjsvar.jq.popup.find("label").text("Move selected files to folder:");
				moverestorepopjsvar.jq.popup.find(".btnBar .actionBtnjs").html("<span>Move</span>");
				moverestorepopjsvar.open();
			}
			if (type == "restore") {
				this.folderfill($('#moverestoreSelect'+ this.arg.id.substr(1)),true);
				moverestorepopjsvar.arg.dialogType=type;
				moverestorepopjsvar.jq.popupDialog.find(".ui-dialog-title").text("Restore File(s)");
				moverestorepopjsvar.jq.popup.find("label").text("Restore to folder:");
				moverestorepopjsvar.jq.popup.find(".btnBar .actionBtnjs").html("<span>Restore Files</span>");
				moverestorepopjsvar.open();
			}
			if (type == "uploadfiles") {
				this.folderfill($('#uploadFolderSelect'+ this.arg.id.substr(1)),false);
				uploadfilepopjsvar.arg.numselecteddocs=0;
				uploadfilepopjsvar.arg.invalidsize = 0; 
				uploadfilepopjsvar.arg.invalidtype = 0; 
				uploadfilepopjsvar.arg.duplicatelabel = 0; 
				uploadfilepopjsvar.arg.nolabel = 0; 
				uploadfilepopjsvar.open();
			}
			return false;
		},
		permdelete: function(){
			var documents={};
			if (this.folderselected!=null){
				var activefolder = this.folders[this.folderselected];
				var y;
				for (y in activefolder.docs){
					if(activefolder.docs[y].isSelected){
						activefolder.docs[y].jq.id.remove();
						documents[activefolder.docs[y].arg.id]=activefolder.docs[y];
						delete activefolder.docs[y];
						activefolder.numdocs--;
					}
				}
				activefolder.showfolder();
				tcoipc.fire(this.event("docpermdelete"), {folder:activefolder,documents:documents});
				permdeletepop.close();
			}
		},
		markunread: function() {
			if (this.folderselected!=null){
				var activefolder = this.folders[this.folderselected];
				var y;
				for (y in activefolder.docs){
					if(activefolder.docs[y].isSelected){
						activefolder.docs[y].jq.id.addClass("unread");
						activefolder.docs[y].arg.unread=true;
					}
						
				}
			}
		},
		processPopup: function(foldertype, selecteddoc){
			var id=this.arg.id.substr(1);
			var selectedfolder ="NONE";
			if (!foldertype){
				foldertype="select";
			} 
			if (foldertype=="select"){
				selectedfolder=$("#moverestoreSelect"+id).val();
			}
			else if (foldertype=="trash"){
				var i;
				for (i in this.folders){
					if (this.folders[i].arg.type==foldertype){
						selectedfolder=i;
					}
				}
			}
			else if (foldertype=="drop"){
				if(selecteddoc){
					selecteddoc.document.select(true);
					selectedfolder=selecteddoc.folder.arg.id;
				}
			}
			if(selectedfolder!="NONE"){
				var fromfolder=this.folderselected;
				var tofolder=selectedfolder;
				var i;
				for(i in this.folders[fromfolder].docs){
					if(this.folders[fromfolder].docs[i].isSelected){
						this.folders[tofolder].docs[i]=this.folders[fromfolder].docs[i];
						delete this.folders[fromfolder].docs[i];
						this.folders[tofolder].jq.table.append(this.folders[tofolder].docs[i].jq.id);
						this.folders[tofolder].docs[i].select(false);
						this.folders[tofolder].docs[i].arg.parent=this.folders[tofolder];
						this.folders[fromfolder].numdocs--;
						this.folders[tofolder].numdocs++;
						tcoipc.fire(this.event("docMove"),{toFolder: this.folders[tofolder], fromFolder: this.folders[fromfolder], document:this.folders[tofolder].docs[i]});
						if (this.folders[tofolder].tablesort) {
							this.folders[tofolder].tablesort.reloadSortData();
							tcoipc.fire(this.folders[tofolder].tablesort.event("afterSort"),null);
						}
						if (this.folders[fromfolder].tablesort) {
							this.folders[fromfolder].tablesort.reloadSortData();
							tcoipc.fire(this.folders[fromfolder].tablesort.event("afterSort"),null);
						}
					}
				}
				this.folders[this.folderselected].showfolder();
				tcoipc.fire(this.event("folderSelected"),{folder: this.folders[this.folderselected]});
			}
			moverestorepopjsvar.close();
			return false;
		}
};

/* ------------------------------------------------------ WINDOW ACTIVE ------------------------------------------- */
tiaacref.WindowActive=true;


/* tcmenu */


var TIAA = TIAA || {};
/*
TIAA.MegaMenu = (function(){
	var $navUl, align, fill;
	return {
		init : function($id, align, fill){
			align = align || 'left';
			fill = fill || false;
			if (fill){
				TIAA.MegaMenu.fillNav($id);
			}
			$navUl = $('.utillinks').length ? $id.add('.utillinks > ul') : $id;
			$id.each(function(i,menu){
				TIAA.MegaMenu.setPos($(menu), align);
			});
			TIAA.MegaMenu.eventBindings();
			tiaacref.setMegaMenuSelected();
			tiaacref.setMenuAnalytics();
			$("ul.mm_list li:not(:has(a))").each(function() {
				$(this).html( "<span role='menuitem' class='mm-title'>" + $(this).text() + "</span>");
			});
			$(".mm_callout ul li a").attr("tabindex","-1").attr("role","menuitem");
		},
		setPos : function($menu, align){
			var menuLeftPos = $menu.offset().left,
				menuWidth = menuLeftPos + $menu.outerWidth();
			$menu.find('.megamenu').css({'visibility' : 'hidden', 'display' : 'block', 'z-index': 100}).each(function(i,mega){
				var $mega = $(mega),
					megaWidth = $mega.outerWidth(true),
					megaLeftPos = $mega.closest('li').offset().left,

					megaRightPos = megaLeftPos + megaWidth,
					menuMegaLeftDiff =  menuLeftPos-megaLeftPos,
					megaLeftPosNew;
				if (align === 'left'){
					megaLeftPosNew = isNaN(parseInt($mega.css('left'), 10)) ? 0 : parseInt($mega.css('left'), 10);
					if (megaRightPos > menuWidth){
						megaLeftPosNew = ((megaRightPos - menuWidth - 2) * -1) + megaLeftPosNew;
					}
				}
				if (align === 'right'){
					if($mega.closest('li').hasClass('last')){
					megaLeftPosNew = $mega.closest('li').outerWidth(true) - megaWidth +1;}
				else{
					megaLeftPosNew = $mega.closest('li').outerWidth(true) - megaWidth;}
					if (megaLeftPosNew < menuMegaLeftDiff){
						megaLeftPosNew = menuLeftPos - megaLeftPos;
					}
				}
				$(mega).css('left', megaLeftPosNew);
			}).css({'visibility' : '', 'display' : ''});
		},
		eventBindings : function(){
			$navUl.find('.megamenu').closest('li').on({
				'mouseenter.megaMenu' : TIAA.MegaMenu.showMenu,
				'mouseleave.megaMenu' : TIAA.MegaMenu.clearMenus,
				'keydown.megaMenu' : function(e){
					var keys = {enter:13, esc:27, tab:9, left:37, up:38, right:39, down:40, spacebar:32},
						$this = $(this);

					switch(e.keyCode) {
						case keys.down:
							if ($this.children('a').hasClass('megamenuhover')){
								$this.children('a').blur().next().find('a').first().focus();
							} else {
								TIAA.MegaMenu.showMenu.apply($this);
							}
							return false;
						case keys.up:
							if ($this.children('a').hasClass('megamenuhover') && $this.children('a:focus').length){
								TIAA.MegaMenu.clearMenus();
							}
							return false;
						case keys.esc:
							TIAA.MegaMenu.clearMenus();
							return false;
						case keys.tab:
							TIAA.MegaMenu.clearMenus();
							return true;
						case keys.spacebar:
							TIAA.MegaMenu.clickAnchors.apply($this);
							return false;
						case keys.enter:
							TIAA.MegaMenu.clickAnchors.apply($this);
							return false;
						default:
							return true;
					}
				},
				'click.megaMenu touchstart.megaMenu' : function(e){
					var $this = $(this);
					e.preventDefault();
					e.stopPropagation();
					TIAA.MegaMenu.clickAnchors.apply($this);
				},
				'focusin.megaMenu' : function(){
					var $this = $(this);
					TIAA.MegaMenu.showMenu.apply($this);
				}
			});
			$navUl.children('li').on('keydown.megaMenu', function(e){
				var keys = {left:37, right:39},
					$this = $(this);

				switch(e.keyCode) {
					case keys.right:
						TIAA.MegaMenu.nextNav.apply($this);
						return false;
					case keys.left:
						TIAA.MegaMenu.prevNav.apply($this);
						return false;
					default:
						return true;
				}
			});
			$('.megamenu').on('click touchstart', function(e){e.stopPropagation();});
			$('.megamenu').find('a,button').on({
				keydown : function(e){
					var keys = {enter:13, esc:27, tab:9, left:37, up:38, right:39, down:40, spacebar:32},
						$this = $(this);

					switch(e.keyCode) {
						case keys.down:
							e.stopPropagation();
							TIAA.MegaMenu.nextSubNav.apply($this);
							return false;
						case keys.up:
							e.stopPropagation();
							TIAA.MegaMenu.prevSubNav.apply($this);
							return false;
						case keys.spacebar:
							e.stopPropagation();
							document.location.href = $this.attr('href');
							return false;
						case keys.enter:
							e.stopPropagation();
							document.location.href = $this.attr('href');
							return false;
						case keys.esc:
							$this.blur().closest('.megamenu').prev().focus();
						case keys.tab:
							$this.blur().closest('.megamenu').prev().focus();
						default:
							return true;
					}
				},
				click : function(e){
					TIAA.MegaMenu.clearMenus();
					e.stopPropagation();
				}
			});
			if (Modernizr.touch){
				$('body').on('click touchstart', function(){
					if ($('.megamenu').is(':visible')){
						TIAA.MegaMenu.clearMenus();
					}
				});
			}
		},
		clickAnchors : function(){
			var $this = $(this),
				$thisAnchor = $this.children('a');
			if ($thisAnchor.hasClass('megamenuhover') || $this.attr('aria-haspopup') == 'false'){
				document.location.href = $thisAnchor.attr('href');
				TIAA.MegaMenu.clearMenus();
			} else {
				TIAA.MegaMenu.clearMenus();
				TIAA.MegaMenu.showMenu.apply($this);
			}
			return false;
		},
		showMenu : function(){
			$(this).find('.megamenu').show().attr('aria-hidden', 'false').removeClass('hidden').prev().addClass('megamenuhover');
		},
		clearMenus : function(){
			$navUl.find('.megamenu').hide().attr('aria-hidden', 'true').prev().removeClass('megamenuhover');
		},
		nextNav : function(){
			var $this = $(this);
			TIAA.MegaMenu.clearMenus();
			if ($this.next().length){
				$this.blur().next().find('>a').focus();
			}
		},
		prevNav : function(){
			var $this = $(this);
			TIAA.MegaMenu.clearMenus();
			if ($this.prev().length){
				$this.blur().prev().find('>a').focus();
			} else {
				return false;
			}
		},
		nextSubNav : function(){
			var $this = $(this),
				$aCollection = $this.closest('.megamenu').find('a,button'),
				i;
			$aCollection.each(function(i, a){
				if (i === $aCollection.length-1){
					return false;
				} else {
					if ($(a).is(':focus')){
						$this.blur();
						$aCollection.eq(i+1).focus();
						return false;
					}
				}
			});
		},
		prevSubNav : function(){
			var $this = $(this),
				$aCollection = $this.closest('.megamenu').find('a,button'),
				i;
			$aCollection.each(function(i, a){
				if ($(a).is(':focus')){
					if (i === 0){
						$this.blur();
						$this.closest('.megamenu').prev().focus();
					} else {
						$this.blur();
						$aCollection.eq(i-1).focus();
						return false;
					}
				}
			});
		},
		fillNav : function($menu){
			var $menu = $menu,
				menuWidth = $menu.width(),
				liWidth = 0,
				widthDiff = 0;

			$menu.children('li').each(function(i,li){
				liWidth += $(li).outerWidth();
			});

			widthDiff = parseInt((menuWidth - liWidth) / $menu.children('li').length);

			$menu.children('li').each(function(i,li){
				var newWidth = $(li).width() + widthDiff;

				if (i == $menu.children('li').length-1){
					if (widthDiff*$menu.children('li').length < (menuWidth - liWidth)){
						newWidth = newWidth + ((menuWidth - liWidth) - (widthDiff*$menu.children('li').length));
					}
				}

				$(li).width(newWidth).children('a').css({
					'padding-left' : 0,
					'padding-right' : 0,
					'text-align' : 'center',
					'width' : newWidth
				});
			});
		}
	};
}());
*/

TIAA.MegaMenu = (function(){
	var $navUl,
		planFocusSite,
		isPlanFocusSite;
	return {
		init : function($id, align, fill){
			planFocusSite = $('.l1nav .side');
			isPlanFocusSite = (planFocusSite.length > 0 ? true : false);
			align = align || 'left';
			fill = fill || false;
			if (planFocusSite.length > 0) {
//				$('.l1nav .side').off('.megaMenu');		// plan focus fix.
				planFocusSite.off('.megaMenu');
				fill = false;
			}
			if (fill){
				$id.each(function(index, element) {
					TIAA.MegaMenu.fillNav($(this));
				});
			}
			$navUl = $('.utillinks').length ? $id.add('.utillinks > ul') : $id;
			$('.megamenu').find('a, button, input').attr('tabindex', '-1');
			$id.each(function(i,menu){
				TIAA.MegaMenu.setWidthPos($(menu), align);
			});
			TIAA.MegaMenu.eventBindings();
			tiaacref.setMegaMenuSelected();
			tiaacref.setMenuAnalytics();
			$("ul.mm_list li:not(:has(a))").each(function() {
				$(this).html( "<span role='menuitem' class='mm-title'>" + $(this).text() + "</span>");
			});
			$(".mm_callout ul li a").attr("tabindex","-1").attr("role","menuitem");
		},
		setWidthPos : function($menu, align){
			planFocusSite = $('.l1nav .side');
			isPlanFocusSite = (planFocusSite.length > 0 ? true : false);
			var navEnd = $menu.offset().left + $menu.width();
			
			if (isPlanFocusSite) {
				$menu.find('div.megamenu').each(function(i,mega){
					var $mega = $(mega),
						rowWidth = 0,
						leftPos = isNaN(parseInt($mega.css('left'), 10)) ? 0 : parseInt($mega.css('left'), 10),
						dropEnd;
	
					$mega.find('div.mm_col').each(function(i,div){
						rowWidth += $(div).outerWidth(true);
					});
	
					dropEnd = $mega.closest('li').offset().left + rowWidth;
	
					if (dropEnd > navEnd){
						leftPos = ((dropEnd - navEnd + 2) * -1) + leftPos;
					}
					$(mega).css({
						'width' : rowWidth,
						'left' : leftPos,
						'z-index' : 100
					});
				});
			} else {
				var menuLeftPos = $menu.offset().left,
					menuWidth = menuLeftPos + $menu.outerWidth();
				$menu.find('.megamenu').css({'visibility' : 'hidden', 'display' : 'block', 'z-index': 100}).each(function(i,mega){
					var $mega = $(mega),
						megaWidth = $mega.outerWidth(true),
						megaLeftPos = $mega.closest('li').offset().left,
	
						megaRightPos = megaLeftPos + megaWidth,
						menuMegaLeftDiff =  menuLeftPos-megaLeftPos,
						megaLeftPosNew;
					if (align === 'left'){
						megaLeftPosNew = isNaN(parseInt($mega.css('left'), 10)) ? 0 : parseInt($mega.css('left'), 10);
						if (megaRightPos > menuWidth){
							megaLeftPosNew = ((megaRightPos - menuWidth - 2) * -1) + megaLeftPosNew;
						}
					}
					if (align === 'right'){
						if($mega.closest('li').hasClass('last')){
						megaLeftPosNew = $mega.closest('li').outerWidth(true) - megaWidth +1;}
					else{
						megaLeftPosNew = $mega.closest('li').outerWidth(true) - megaWidth;}
						if (megaLeftPosNew < menuMegaLeftDiff){
							megaLeftPosNew = menuLeftPos - megaLeftPos;
						}
					}
					$(mega).css('left', megaLeftPosNew);
				}).css({'visibility' : '', 'display' : ''});
			}
		},
		eventBindings : function(){
			//Bind to only LI's with a .megamenu child element
			$navUl.find('div.megamenu').closest('li').on({
				'mouseenter.megaMenu' : TIAA.MegaMenu.showMenu,
				'mouseleave.megaMenu' : TIAA.MegaMenu.clearMenus,
				'keydown.megaMenu' : function(e){
					var keys = {enter:13, esc:27, tab:9, left:37, up:38, right:39, down:40, spacebar:32},
						$this = $(this);

					switch(e.keyCode) {
						case keys.down:
							if ($this.children('a').hasClass('megamenuhover')){
								$this.children('a').blur().next().find('a').first().focus();
							} else {
								TIAA.MegaMenu.showMenu.apply($this);
							}
							return false;
						case keys.up:
							if ($this.children('a').hasClass('megamenuhover') && $this.children('a:focus').length){
								TIAA.MegaMenu.clearMenus();
							}
							return false;
						case keys.esc:
							TIAA.MegaMenu.clearMenus();
							return false;
						case keys.tab:
							TIAA.MegaMenu.clearMenus();
							return true;
						case keys.spacebar:
							TIAA.MegaMenu.clickAnchors.apply($this);
							return false;
						case keys.enter:
							TIAA.MegaMenu.clickAnchors.apply($this);
							return false;
						default:
							return true;
					}
				},
				'click.megaMenu touchstart.megaMenu' : function(e){
					var $this = $(this);
					e.preventDefault();
					e.stopPropagation();
					TIAA.MegaMenu.clickAnchors.apply($this);
				},
				'focusin.megaMenu' : function(){
					var $this = $(this);
					TIAA.MegaMenu.showMenu.apply($this);
				}
			});
			//Bind to all LI's to allow left/right navigation from elements without .megamenu's
			$navUl.children('li').on('keydown.megaMenu', function(e){
				var keys = {left:37, right:39},
					$this = $(this);

				switch(e.keyCode) {
					case keys.right:
						TIAA.MegaMenu.nextNav.apply($this);
						return false;
					case keys.left:
						TIAA.MegaMenu.prevNav.apply($this);
						return false;
					default:
						return true;
				}
			});
			$('.megamenu').on('click touchstart', function(e){e.stopPropagation();});
			$('.megamenu').find('a,button').on({
				keydown : function(e){
					var keys = {enter:13, esc:27, tab:9, left:37, up:38, right:39, down:40, spacebar:32},
						$this = $(this);

					switch(e.keyCode) {
						case keys.down:
							e.stopPropagation();
							TIAA.MegaMenu.nextSubNav.apply($this);
							return false;
						case keys.up:
							e.stopPropagation();
							TIAA.MegaMenu.prevSubNav.apply($this);
							return false;
						case keys.spacebar:
							e.stopPropagation();
							document.location.href = $this.attr('href');
							return false;
						case keys.enter:
							e.stopPropagation();
							document.location.href = $this.attr('href');
							return false;
						case keys.esc:
							$this.blur().closest('div.megamenu').prev().focus();
						case keys.tab:
							$this.blur().closest('div.megamenu').prev().focus();
						default:
							return true;
					}
				},
				click : function(e){
					TIAA.MegaMenu.clearMenus();
					e.stopPropagation();
				}
			});
			if (Modernizr.touch){
				$('body').on('click touchstart', function(){
					if ($('.megamenu').is(':visible')){
						TIAA.MegaMenu.clearMenus();
					}
				});
			}
		},
		clickAnchors : function(){
			var $this = $(this),
				$thisAnchor = $this.children('a');
			if ($thisAnchor.hasClass('megamenuhover') || ($this.attr('aria-haspopup') == 'false' && $thisAnchor.attr('aria-haspopup') != 'true')) {
//			if ($thisAnchor.hasClass('megamenuhover') || $this.attr('role') !== 'menu'){
				document.location.href = $thisAnchor.attr('href');
				TIAA.MegaMenu.clearMenus();
			} else {
				TIAA.MegaMenu.clearMenus();
				TIAA.MegaMenu.showMenu.apply($this);
			}
			return false;
		},
		showMenu : function(){
			$(this).find('div.megamenu').show().attr('aria-hidden', 'false').removeClass('hidden').prev().addClass('megamenuhover');
		},
		clearMenus : function(){
			$navUl.find('div.megamenu').hide().attr('aria-hidden', 'true').prev().removeClass('megamenuhover');
		},
		nextNav : function(){
			var $this = $(this);
			TIAA.MegaMenu.clearMenus();
			if ($this.next().length){
				$this.blur().next().find('>a').focus();
			}
		},
		prevNav : function(){
			var $this = $(this);
			TIAA.MegaMenu.clearMenus();
			if ($this.prev().length){
				$this.blur().prev().find('>a').focus();
			} else {
				return false;
			}
		},
		nextSubNav : function(){
			var $this = $(this),
				$aCollection = $this.closest('div.megamenu').find('a,button'),
				i;
			$aCollection.each(function(i, a){
				if (i === $aCollection.length-1){
					return false;
				} else {
					if ($(a).is(':focus')){
						$this.blur();
						$aCollection.eq(i+1).focus();
						return false;
					}
				}
			});
		},
		prevSubNav : function(){
			var $this = $(this),
				$aCollection = $this.closest('div.megamenu').find('a,button'),
				i;
			$aCollection.each(function(i, a){
				if ($(a).is(':focus')){
					if (i === 0){
						$this.blur();
						$this.closest('div.megamenu').prev().focus();
					} else {
						$this.blur();
						$aCollection.eq(i-1).focus();
						return false;
					}
				}
			});
		},
		fillNav : function($menu){
			var $menu = $menu,
				menuWidth = $menu.width(),
				liWidth = 0,
				widthDiff = 0;

			$menu.children('li').each(function(i,li){
				liWidth += $(li).outerWidth();
			});

			widthDiff = parseInt((menuWidth - liWidth) / $menu.children('li').length);

			$menu.children('li').each(function(i,li){
				var newWidth = $(li).width() + widthDiff;

				if (i == $menu.children('li').length-1){
					if (widthDiff*$menu.children('li').length < (menuWidth - liWidth)){
						newWidth = newWidth + ((menuWidth - liWidth) - (widthDiff*$menu.children('li').length));
					}
				}

				$(li).width(newWidth).children('a').css({
					'padding-left' : 0,
					'padding-right' : 0,
					'text-align' : 'center',
					'width' : newWidth
				});
			});
		}
	};
}());
//TIAA.MegaMenu.init($('.l1nav'));

//$('.l1nav .side').off('.megaMenu')

/*
* IFA mega menu
*/

TIAA.IFAMegaMenu = (function(){
	var $navUl;
	return {
		init : function(id){
			$navUl = id;
			$('.megamenu').find('a').attr('tabindex', '-1');
			$navUl.each(function(i,menu){
				TIAA.IFAMegaMenu.setWidthPos($(menu));
			});
			TIAA.IFAMegaMenu.linkBindings();
		},
		setWidthPos : function($menu){
			var navEnd = $menu.offset().left + $menu.width();

			$menu.find('.megamenu').each(function(i,mega){
				var $mega = $(mega),
					rowWidth = 0,
					leftPos = isNaN(parseInt($mega.css('left'), 10)) ? 0 : parseInt($mega.css('left'), 10),
					dropEnd;

				$mega.children('div').each(function(i,div){
					if ($(div).hasClass('col')) {
						rowWidth += $(div).outerWidth(true);
					} else if ($(div).hasClass('clear')) {
						return false;
					}
				});

				dropEnd = $mega.closest('li').offset().left + rowWidth;

				if (dropEnd > navEnd){
					leftPos = ((dropEnd - navEnd + 2) * -1) + leftPos;
				}
				$(mega).css({
					'width' : rowWidth,
					'left' : leftPos,
					'z-index' : 100
				});
			});
		},
		linkBindings : function(){
			$navUl.children('li').on({
				'mouseenter.megaMenu' : TIAA.IFAMegaMenu.showMenu,
				'mouseleave.megaMenu' : TIAA.IFAMegaMenu.clearMenus,
				'keydown.megaMenu' : function(e){
					var keys = {enter:13, esc:27, tab:9, left:37, up:38, right:39, down:40, spacebar:32},
						$this = $(this);

					switch(e.keyCode) {
						case keys.down:
							if ($this.children('a').hasClass('megamenuhover')){
								$this.children('a').blur().next().find('a').first().focus();
							} else {
								TIAA.IFAMegaMenu.showMenu.apply($this);
							}
							return false;
						case keys.up:
							if ($this.children('a').hasClass('megamenuhover') && $this.children('a:focus').length){
								TIAA.IFAMegaMenu.clearMenus();
							}
							return false;
						case keys.right:
							TIAA.IFAMegaMenu.nextNav.apply($this);
							return false;
						case keys.left:
							TIAA.IFAMegaMenu.prevNav.apply($this);
							return false;
						case keys.esc:
							TIAA.IFAMegaMenu.clearMenus();
							return false;
						case keys.tab:
							TIAA.IFAMegaMenu.clearMenus();
							return true;
						case keys.spacebar:
							e.preventDefault();
							TIAA.IFAMegaMenu.clickAnchors.apply($this);
							$this.children('a').click();
							document.location.href = $this.children('a').attr('href');
							return false;
						case keys.enter:
							TIAA.IFAMegaMenu.clickAnchors.apply($this);
							return true;
						default:
							return true;
					}
				},
				'click.megaMenu' : function(e){
					var $this = $(this);
					TIAA.IFAMegaMenu.clickAnchors.apply($this);
				},
				'focusin.megaMenu' : function(){
					var $this = $(this);
					TIAA.IFAMegaMenu.showMenu.apply($this);
				}
			});
			$('.megamenu').on('click', function(e){e.stopPropagation();});
			$('.megamenu').find('a,button').on({
				keydown : function(e){
					var keys = {enter:13, esc:27, tab:9, left:37, up:38, right:39, down:40, spacebar:32},
						$this = $(this);

					switch(e.keyCode) {
						case keys.down:
							e.stopPropagation();
							TIAA.IFAMegaMenu.nextSubNav.apply($this);
							return false;
						case keys.up:
							e.stopPropagation();
							TIAA.IFAMegaMenu.prevSubNav.apply($this);
							return false;
						case keys.spacebar:
							e.stopPropagation();
							document.location.href = $this.attr('href');
							return false;
						case keys.enter:
							e.stopPropagation();
							document.location.href = $this.attr('href');
							return false;
						case keys.esc:
							$this.blur().closest('.megamenu').prev().focus();
						case keys.tab:
							$this.blur().closest('.megamenu').prev().focus();
						default:
							return true;
					}
				},
				click : function(e){
					TIAA.IFAMegaMenu.clearMenus();
					e.stopPropagation();
				}
			});
		},
		clickAnchors : function(){
			var $this = $(this),
				$thisAnchor = $this.children('a'),
				hasMegaMenu = $this.children('div.megamenu').length === 0 ? false : true;
			if ($thisAnchor.hasClass('megamenuhover') || $this.attr('role') !== 'menu' || hasMegaMenu === false ){
				return true;
			} else {
				TIAA.IFAMegaMenu.showMenu.apply($this);
			}
			return false;
		},
		showMenu : function(){
			$(this).find('.megamenu').show().attr('aria-hidden', 'false').removeClass('hidden').prev().addClass('megamenuhover');
		},
		clearMenus : function(){
			$navUl.find('.megamenu').hide().attr('aria-hidden', 'true').prev().removeClass('megamenuhover');
		},
		nextNav : function(){
			var $this = $(this);
			TIAA.IFAMegaMenu.clearMenus();
			if ($this.next().length){
				$this.blur().next().find('>a').focus();
			}
		},
		prevNav : function(){
			var $this = $(this);
			TIAA.IFAMegaMenu.clearMenus();
			if ($this.prev().length){
				$this.blur().prev().find('>a').focus();
			} else {
				return false;
			}
		},
		nextSubNav : function(){
			var $this = $(this),
				$aCollection = $this.closest('.megamenu').find('a,button'),
				i;
			$aCollection.each(function(i, a){
				if (i === $aCollection.length-1){
					return false;
				} else {
					if ($(a).is(':focus')){
						$this.blur();
						$aCollection.eq(i+1).focus();
						return false;
					}
				}
			});
		},
		prevSubNav : function(){
			var $this = $(this),
				$aCollection = $this.closest('.megamenu').find('a,button'),
				i;
			$aCollection.each(function(i, a){
				if ($(a).is(':focus')){
					if (i === 0){
						$this.blur();
						$this.closest('.megamenu').prev().focus();
					} else {
						$this.blur();
						$aCollection.eq(i-1).focus();
						return false;
					}
				}
			});
		}
	};
}());
/* ------------------------ set events -------------------------- */
tiaacref.setEvents = function (events) {
	var i;
	for (i in events) {
		tcoipc.on(i, events[i]);
	}
};



/* --------------------------- AnalyticsDatLayer ----------------------- 
 * 
 * 	Source: http://www.w3.org/2013/12/ceddl-201312.pdf
 * 
 * 
 * 		QUESTIONS:
 * 			prop2 - how is this an event.eventInfo
 * 			prop11 - a new event? (DONE??)
 * 			prop13 - a new event? 
 * 			prop20 - a new event? 
 * 			prop34 - a new event? 
 * 			prop35 - a new event? 
 * 			prop21 to prop25, prop29, hier1 - OpinionLab 
 * 			prop37 - unknown component
 * 			list1 - what is 'path'
 * 			list2 - unknown component
 * 			
 */
tiaacref.analyticsTrackLink = {};
tiaacref.adl = {
	tracers: {
		components: {},
		events: {},
		users: {}
	},
	objects: {
		component: function (arg) {
			if (typeof arg.attributes !== 'object') {
				arg.attributes = {};
			}
			this.componentInfo = {
				componentID: (typeof arg.componentID !== 'string' ? "" : arg.componentID),
				componentName: (typeof arg.componentName !== 'string' ? "" : arg.componentName)
			};
			this.category = {
				primaryCategory: ""
			};
			this.attributes = {};
			$.extend(this.attributes, {}, arg.attributes)
		},
		event: function (arg) {
			if (typeof arg === 'string') {
				arg = {
					linkName: arg,
					linkType: 'o',
					attributes: {
						linkTrackVars: 'prop34,pageName,prop33',
						linkTrackEvents: '',
						events: '',
						isLink: true
					}
				};
			}
			this.eventInfo = {
				eventName: (typeof arg.linkName !== 'string' ? "TestEventName" : arg.linkName),
				eventAction: "",
				eventPoints: 0,
				type: (typeof arg.linkType !== 'string' ? "o" : ("oed".indexOf(arg.linkType) === -1 ? "o" : arg.linkType)),
				timeStamp: new Date(),
				effect: ""
			};
			this.category = {
				primaryCategory: ""
			};
			arg.attributes = (typeof arg.attributes !== 'object' ? {} : arg.attributes);
			$.extend(arg.attributes, {}, {
				linkTrackVars: (!arg.attributes.linkTrackVars ? 'prop34,pageName,prop33' : arg.attributes.linkTrackVars),
				linkTrackEvents: (!arg.attributes.linkTrackEvents ? '' : arg.attributes.linkTrackEvents),
				event: (!arg.attributes.events ? '' : arg.attributes.events),
				isLink: arg.attributes.isLink
				}
			);
			this.attributes = arg.attributes;
		},
		profile: function () {
			this.profileInfo = {
				profileID: "",
				userName: "",
				email: "",
				language: "",
				returningStatus: "",
				type: ""
			};
			this.address = {
				line1: "",
				line2: "",
				city: "",
				stateProvince: "",
				postalCode: "",
				country: ""
			};
			this.attributes = {};
		},
		user: function () {
			this.segment = {};
			this.profile = [new tiaacref.adl.objects.profile()];
		}
	},
	initialize: function () {
		var i;
		digitalData = {
			pageInstanceID: "",
			page: {
				pageInfo: {
					pageID: "",
					pageName: "",
					destinationURL: document.location,
					referringURL: document.referrer,
					sysEnv: "",
					variant: "",
					version: "",
					breadCrumbs: [], 
					author: "TIAA-CREF",
					issueDate: "",							// TODO: Automatically create date
					effectiveDate: "",						// TODO: Automatically create date
					expiryDate: "",							// TODO: Automatically create date 
					language: "en-US",						// TODO: Automatically set
					geoRegion: "US",						// TODO: Automatically set
					industryCodes: "",
					publisher: "TIAA-CREF"
					
				},
				category: {
					primaryCategory: ""
				},
				attributes: {}
			},
			user: [new tiaacref.adl.objects.user()],
			product: [],
			cart: {},
			transaction: {},
			event: [],
			/*
			 * Component Objects (Media??)
			 * 		digitalData.component[n].componentInfo = {
			 * 			componentID: "rog300v",
			 * 			}; 
			 * 
			 *		digitalData.component[n].category = {
			 *			primaryCategory: "Haircare"
			 *			}; 
			 *
			 *		digitalData.component[n].attributes = {};
			 *
			 */
			component: [],
			privacyAccessCategories: {},
			version: {} 
		}
	},
	map: function (variable, value) {
		var events,
			i,
			l,
			obj;
		if (variable.substr(0,4) === 'prop') {
			switch (variable) {
			case 'prop1':
			case 'prop8':
			case 'prop17':
			case 'prop30':
			case 'prop31':
			case 'prop32':
				digitalData.page.pageInfo[variable] = value;
				break;
			case 'prop3':
			case 'prop14':
			case 'prop16':
			case 'prop18':
			case 'prop19':
			case 'prop27':
			case 'prop28':
			case 'prop36':
			case 'prop40':
			case 'prop41':
			case 'prop42':
				digitalData.user[0][variable] = value;
				break;
			case 'prop11':
				obj = new tiaacref.adl.objects.event({eventName: value});
				digitalData.event.push(obj);
			}
			
		} else if (variable.substr(0,4) === 'eVar') {
			switch (variable) {
			case 'eVar1':
				digitalData.page.pageInfo[variable] = value;
				break;
			}
		} else if (variable.substr(0,4) === 'list') {
			
		} else if (variable.substr(0,4) === 'hier') {
			if (variable === 'hier2') {
				digitalData.page.pageInfo[variable] = value;
			}

		} else {
			switch (variable) {
			case 'component':
				obj = new tiaacref.adl.objects.component(value);
				digitalData.component.push(obj);
				return digitalData.component.length - 1;
				break;
			case 'event':
				obj = new tiaacref.adl.objects.event(value);
				digitalData.event.push(obj);
				return digitalData.event.length - 1;
				break;
			case 'pageName':
			case 'channel':
			case 'siteSubSection':
			default:
				digitalData.page.pageInfo[variable] = value;
				break;
			case 'server':
				break;
			case 'pageType':
				break;
			case 'products':
				digitalData.products = value;
				break;
			case 'campaign':
				break;
			case 'state':
				break;
			case 'zip':
				break;
			}
		}
	}
};




/* ------------------------------------- TC DOCS, FOLDERS, and MANAGER -------------------------------- */
tiaacref.defaults.TCDocument = {
	id: '',
	style: '',
	eventName: {
		remove: "remove",
		move: "move",
		restore: "restore",
		read: "read"
	}
};

tiaacref.TCDocument = function (arg) {
	var defaults = tiaacref.defaults.TCDocument,
		self = this;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style,
			parent: null
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (!arg.parent instanceof tiaacref.TCFolder) {
			arg.parent = null;
		}
	}
	this.arg = arg;
	this.arg.originalParent = null;
//	this.arg.originalParent = this.arg.parent;
	this.isSelected = false;
	this.isDeleted = false;
	this.isRead = arg.isRead;
	tcoipc.on(this.event('remove'), function () {
		self.remove();
	});
	tcoipc.on(this.event('move'), function (toWhom) {
		self.move(toWhom);
	});
	tcoipc.on(this.event('restore'), function (toWhom) {
		self.restore(toWhom);
	});
	tcoipc.on(this.event('read'), function (flag) {
		self.markAsRead(flag);
	});
	return;
};
tiaacref.TCDocument.prototype = {
	remove: function () {
		this.isDeleted = true;
		this.tellParent('documentBeforeRemove');
		this.tellParent('documentRemove');
	},
	markAsRead: function (flag) {
		this.isRead = flag;
		this.tellParent('documentRead');
	},
	select: function (flag, fireEvent) {
		if (typeof fireEvent !== 'boolean') {
			fireEvent = true;
		}
		this.isSelected = flag;
		if (fireEvent) {
			this.tellParent('documentSelected');
		}
	},
	restore: function (toWhom) {
		if (this.arg.parent != null) {
			if (this.arg.parent.arg.isTrash) {
				this.move(toWhom);
			}
		} else {
			this.move(toWhom);
		}
		this.isDeleted = false;
	},
	move: function (toWhom) {
		if (toWhom instanceof tiaacref.TCFolder) {
			var oldParent = this.arg.parent;
			this.arg.parent = toWhom;
			if (oldParent != null) {
				tcoipc.fire(oldParent.event('documentBeforeMoveFrom'), this);
				tcoipc.fire(oldParent.event('documentMoveFrom'), this);
			}
			tcoipc.fire(toWhom.event('documentBeforeMoveTo'), this);
			tcoipc.fire(toWhom.event('documentMoveTo'), this);
		}
	},
	tellParent: function (event) {
		if (this.arg.parent) {
			tcoipc.fire(this.arg.parent.event(event), this);
		}
	},
	event: function (eventName) {
		return tiaacref.eventName('TCDocument.' + eventName) + this.arg.id;
	}

};


tiaacref.defaults.TCFolder = {
	eventName: {
		documentRemove: "documentRemove",
		documentBeforeRemove: "documentBeforeRemove",
		documentMoveTo: "documentMoveTo",
		documentBeforeMoveTo: "documentBeforeMoveTo",
		documentMoveFrom: "documentMoveFrom",
		documentBeforeMoveFrom: "documentBeforeMoveFrom",
		documentSelected: "documentSelected",
		documentRead: "documentRead"
	}
}


tiaacref.TCFolder = function (arg) {
	var defaults = tiaacref.defaults.TCFolder,
		self = this;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style,
			parent: null
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
		if (!arg.parent instanceof tiaacref.TCManager) {
			arg.parent = null;
		}
	}
	this.jq = {
		id:$(arg.id)
	};
	this.arg = arg;
	this.isSelected = false;
	this.isDeleted = false;
	this.documents = {};
	
	tcoipc.on(this.event('documentRead'), function (document) {
		self.tellParent('documentRead', document);
	});
	tcoipc.on(this.event('documentBeforeRemove'), function (document) {
		self.tellParent('documentBeforeRemove', document);
		return;
	});
	tcoipc.on(this.event('documentRemove'), function (document) {
		if (self.documents[document.arg.id]) {
			delete self.documents[document.arg.id];
			self.tellParent('documentRemove', document);
		}
	});
	tcoipc.on(this.event('documentBeforeMoveTo'), function (document) {
		self.tellParent('documentBeforeMoveTo', document);
		return;
	});
	tcoipc.on(this.event('documentMoveTo'), function (document) {
		if (self.documents[document.arg.id]) {
			delete self.documents[document.arg.id];
			self.tellParent('documentMoveTo', document);
		}
	});
	tcoipc.on(this.event('documentBeforeMoveFrom'), function (document) {
		self.tellParent('documentBeforeMoveFrom', document);
		return;
	});
	tcoipc.on(this.event('documentMoveFrom'), function (document) {
		if (!self.documents[document.arg.id]) {
			self.documents[document.arg.id] = document;
			self.tellParent('documentMoveFrom', document);
		}
	});
	tcoipc.on(this.event('documentSelected'), function (document) {
		self.tellParent('documentSelected', document);
	});
	
	

	
}
tiaacref.TCFolder.prototype = {
	remove: function () {
		this.isDeleted = true;
	},
	tellParent: function (event, document) {
		if (this.arg.parent) {
			tcoipc.fire(this.arg.parent.event(event), {folder: this, document: document});
		}
	},
	event: function (eventName) {
		return tiaacref.eventName('TCFolder.' + eventName) + this.arg.id;
	},
	addDocument: function (documentArg) {
		this.documents[documentArg.id] = new tiaacref.TCDocument($.extend({parent: this}, documentArg));
		return this.documents[documentArg.id];
	}

};




tiaacref.defaults.TCManager = {
	eventName: {
		documentBeforeRemove: 'documentBeforeRemove',
		documentRemove: 'documentRemove',
		documentBeforeMoveTo: 'documentBeforeMoveTo',
		documentMoveTo: 'documentMoveTo',
		documentBeforeMoveFrom: 'documentBeforeMoveFrom',
		documentMoveFrom: 'documentMoveFrom',
		documentSelected: 'documentSelected',
		documentRead: 'documentRead'
	}
};




tiaacref.TCManager = function (arg) {
	var defaults = tiaacref.defaults.TCFolder,
		self = this;
	if (typeof arg !== 'object') {
		arg = {
			id: defaults.id,
			style: defaults.style
		};
	} else {
		if (typeof arg.id !== 'string') {
			arg.id = defaults.id;
		}
		if (typeof arg.style !== 'string') {
			arg.style = defaults.style;
		}
	}
	this.jq = {
		id:$(arg.id)
	};
	this.arg = arg;
	this.isSelected = false;
	this.isDeleted = false;
	this.folders = {};
	
	tcoipc.on(this.event('documentBeforeRemove'), function (folderDocument) {});
	tcoipc.on(this.event('documentRemove'), function (folderDocument) {});
	tcoipc.on(this.event('documentBeforeMoveTo'), function (folderDocument) {});
	tcoipc.on(this.event('documentMoveTo'), function (folderDocument) {});
	tcoipc.on(this.event('documentBeforeMoveFrom'), function (folderDocument) {});
	tcoipc.on(this.event('documentMoveFrom'), function (folderDocument) {});
	tcoipc.on(this.event('documentSelected'), function (folderDocument) {});
	tcoipc.on(this.event('documentRead'), function (folderDocument) {});
	
};

tiaacref.TCManager.prototype = {
	event: function (eventName) {
		return tiaacref.eventName('TCManager.' + eventName) + this.arg.id;
	},
	addFolder: function (folderArg) {
		if (folderArg instanceof tiaacref.TCFolder) {
			this.folders[folderArg.arg.id] = folderArg;
		} else {
			this.folders[folderArg.id] = new tiaacref.TCFolder($.extend({parent: this}, folderArg));
		}
		return this.folders[folderArg.id];
	}

};




tiaacref.defaults.MessageManager = {
	eventName: {
		folderAdd: 'folderAdd',
		documentAdd: 'documentAdd',
		documentSelected: 'documentSelected',
		documentRead: 'documentRead',
		documentMoved: 'documentMoved',
		folderSelectAll: 'folderSelectAll',
		documentRemoved: 'documentRemoved',
		documentNew: 'documentNew',
		documentPrinterFriendlyView: 'documentPrinterFriendlyView',
		analyticsButton: 'analyticsButton',
		analyticsDialog: 'analyticsDialog'
	}
};

tiaacref.MessageManager = function (arg) {
	var id = arg.id.substr(1),
		self = this,
		buttons = arg.data.buttons;
	this.arg = arg;
	this.manager = new tiaacref.TCManager();
	this.folders = {};
	this.folderTypes = {};
	this.activeFolder = null;
	this.jq = {
		id: $(arg.id),
		buttons: {
			newMessage: $('#btnNewMessage' + id),
			trash: $('#btnTrash' + id),
			archive: $('#btnArchive' + id),
			markunread: $('#btnMarkUnread' + id),
			restore: $('#btnRestore' + id),
			permDelete: $('#btnPermDelete' + id)
		},
		folderList: $('#messagesFolderList' + id), 
		folderNameHeader: $('#folderNameHeader' + id),
		toolbar: $('#messagemanagertoolbar' + id),
		tableContainer: $('#tableContainer' + id),
		folders: {
			trash: $('#messagemanagertrash' + id),
			archive: $('#messagemanagerarchive' + id)
		},
		dialogs: {
			dlgRead: {
				to: $('#dlgReadInputTo' + id),
				from: $('#dlgReadInputFrom' + id),
				subject: $('#dlgReadInputSubject' + id),
				date: $('#dlgReadInputDate' + id),
				message: $('#dlgReadInputMessage' + id)
			},
			dlgMessage: {
				to: $('#dlgMessageInputTo' + id),
				from: $('#dlgMessageInputFrom' + id),
				subject: $('#dlgMessageInputSubject' + id),
				topic: $('#dlgMessageInputTopic' + id),
				subjectRe: $('#dlgMessageInputSubjectRe' + id),
				replyMessage: $('#dlgMessageReplyMessage' + id),
				message: $('#dlgMessageInputMessage' + id),
				primaryEmail: $('#dlgMessageInputPrimaryEmail' + id),
				homePhone: $('#dlgMessageInputHomePhone' + id),
				businessPhone: $('#dlgMessageInputBusinessPhone' + id)
			},
			dlgNotification: {
				
			}
		}
	};
	this.dialogs = {
		read: this.arg.data.dialogs.read,
		message: this.arg.data.dialogs.message,
		permDelete: this.arg.data.dialogs.permDelete
	};
	this.toolbar = {
		trash: (buttons.trash == undefined ? null : buttons.trash),
		archive: (buttons.archive == undefined ? null : buttons.archive),
		markunread: (buttons.markunread == undefined ? null : buttons.markunread),
		restore: (buttons.restore == undefined ? null : buttons.restore),
		permDelete: (buttons.permDelete == undefined ? null : buttons.permDelete)
	}
	this.jq.dialogs.dlgMessage.subject.on('change.MessageManger.tiaacref', function (event) {
		self.setTopic();
	})
	this.jq.buttons.markunread.on('click.MessageManager.tiaacref', function (event) {
		event.preventDefault();
		event.stopPropagation();
		tcoipc.fire(self.event('analyticsButton'), {button: self.toolbar.markunread, folder: self.activeFolder})
		self.activeFolder.setRead(false);
	});
	this.jq.buttons.trash.on('click.MessageManager.tiaacref', function (event) {
		event.preventDefault();
		event.stopPropagation();
		tcoipc.fire(self.event('analyticsButton'), {button: self.toolbar.trash, folder: self.activeFolder})
		self.moveDocuments('trash');
	});
	this.jq.buttons.archive.on('click.MessageManager.tiaacref', function (event) {
		event.preventDefault();
		event.stopPropagation();
		tcoipc.fire(self.event('analyticsButton'), {button: self.toolbar.archive, folder: self.activeFolder})
		self.moveDocuments('archive');
	});
	this.jq.buttons.restore.on('click.MessageManager.tiaacref', function (event) {
		event.preventDefault();
		event.stopPropagation();
		tcoipc.fire(self.event('analyticsButton'), {button: self.toolbar.restore, folder: self.activeFolder})
		self.moveDocuments('restore');
	});
	this.jq.buttons.permDelete.on('click.MessageManager.tiaacref', function (event) {
		event.preventDefault();
		event.stopPropagation();
		tcoipc.fire(self.event('analyticsButton'), {button: self.toolbar.perDelete, folder: self.activeFolder})
		self.dialogs.permDelete.arg.processYes = function () {
			self.removeDocuments();
			self.dialogs.permDelete.close();
			self.showActiveFolder();
		};
		self.dialogs.permDelete.open();
	});
	
	
	
	
	
	tcoipc.on(this.event('documentSelected'), function (folderDocument) {
		self.showToolbar();
	});
	
	if (self.dialogs.read != undefined) {
		self.dialogs.read.arg.loadAndOpen = function (document) {
			self.dialogs.read.arg.document = document;
			var me = self.jq.dialogs.dlgRead;
			me.to.text(document.document.arg.to);
			me.from.text(document.document.arg.from);
			me.subject.text(document.document.arg.subject);
			me.date.text(document.document.arg.date.toLocaleString());
			me.message.html(document.document.arg.message);
			self.dialogs.read.jq.popupDialog.find('.ui-dialog-title').text(document.document.arg.subject);
			tiaacref.showElement($('#btnDlgReadArchive' + id));
			tiaacref.showElement($('#btnDlgReadTrash' + id));
			if (document.arg.parent)  {
				if (document.arg.parent.arg.type === 'archive') {
					tiaacref.hideElement($('#btnDlgReadArchive' + id));
				}
				if (document.arg.parent.arg.type === 'trash') {
					tiaacref.hideElement($('#btnDlgReadTrash' + id));
				}
			}
			tcoipc.fire(self.event('analyticsDialog'), {dialog: self.dialogs.read, folder: self.activeeFolder})
			self.dialogs.read.open();
		};
		self.dialogs.read.arg.printerFriendlyView = function () {
			tcoipc.fire(self.event('documentPrinterFriendlyView'), {folder: self.activeFolder, document: self.dialogs.read.arg.document});
			return false;
		}
		self.dialogs.read.arg.process = function (type) {
			var selectedDocument,
				i,
				document,
				closeDialog = true;
			self.dialogs.read.arg.document.markAsRead(true);
			if (type === 'prev') {
				document = self.dialogs.read.arg.document;
				selectedDocument = null;
				closeDialog = false;
				for (i in document.arg.parent.documents) {
					if (i === document.arg.id) {
						break;
					}
					selectedDocument = document.arg.parent.documents[i];
				}
				if (selectedDocument != null) {
					document = selectedDocument;
					self.dialogs.read.arg.document = document;
					var me = self.jq.dialogs.dlgRead;
					me.to.text(document.arg.to);
					me.from.text(document.arg.from);
					me.subject.text(document.arg.subject);
					me.date.text(document.arg.date.toLocaleString());
					me.message.html(document.arg.message);
					self.dialogs.read.jq.popupDialog.find('.ui-dialog-title').text(document.arg.subject);
					self.dialogs.read.resize();
					tcoipc.fire(self.event('analyticsButton'), {button: 'prev', folder: self.activeeFolder})
					tcoipc.fire(self.event('analyticsDialog'), {dialog: self.dialogs.read, folder: self.activeeFolder})
				}
			} else if (type === 'next') {
				document = self.dialogs.read.arg.document;
				selectedDocument = null;
				closeDialog = false;
				for (i in document.arg.parent.documents) {
					if (i === document.arg.id) {
						selectedDocument = document;
					} else if (selectedDocument == document) {
						selectedDocument = document.arg.parent.documents[i];
					}
				}
				if (selectedDocument != null && selectedDocument != document) {
					document = selectedDocument;
					self.dialogs.read.arg.document = document;
					var me = self.jq.dialogs.dlgRead;
					me.to.text(document.arg.to);
					me.from.text(document.arg.from);
					me.subject.text(document.arg.subject);
					me.date.text(document.arg.date.toLocaleString());
					me.message.html(document.arg.message);
					self.dialogs.read.jq.popupDialog.find('.ui-dialog-title').text(document.arg.subject);
					self.dialogs.read.resize();
					tcoipc.fire(self.event('analyticsButton'), {button: 'next', folder: self.activeeFolder})
					tcoipc.fire(self.event('analyticsDialog'), {dialog: self.dialogs.read, folder: self.activeeFolder})
				}
				
			} else if (type === 'trash') {
				if (!self.dialogs.read.arg.document.document.isSelected) {
					self.dialogs.read.arg.document.arg.parent.selectedDocuments ++;		// we must do this because the 'move' decrements this value
				}
				tcoipc.fire(self.event('analyticsButton'), {button: 'trash', folder: self.activeeFolder})
				self.moveDocuments('trash', self.dialogs.read.arg.document);
				
			} else if (type === 'archive') {
				if (!self.dialogs.read.arg.document.document.isSelected) {
					self.dialogs.read.arg.document.arg.parent.selectedDocuments ++;		// we must do this because the 'move' decrements this value
				}
				tcoipc.fire(self.event('analyticsButton'), {button: 'archive', folder: self.activeeFolder})
				self.moveDocuments('archive', self.dialogs.read.arg.document);
			} else if (type === 'reply') {
				self.dialogs.read.close();
				tcoipc.fire(self.event('analyticsButton'), {button: 'reply', folder: self.activeeFolder})
				self.dialogs.message.arg.loadAndOpen(self.dialogs.read.arg.document, "reply");
			} else if (type === 'close') {
			}
			if (closeDialog) {
				self.dialogs.read.close();
				self.showActiveFolder();
			}
			return false;
		}
	}
	if (typeof dlgNotification !== 'undefined') {
		
	}
	
	if (self.dialogs.message != undefined) {
		self.dialogs.message.arg.from = self.arg.data.from;
		self.dialogs.message.arg.subject = "NONE";
		self.dialogs.message.arg.to = 'TIAA-CREF';
		self.dialogs.message.arg.loadAndOpen = function (reply, messageType) {
			messageType = (typeof messageType !== 'string' ? 'new' : messageType);
			self.dialogs.message.arg.reply = reply;
			var me = self.jq.dialogs.dlgMessage;
			self.dialogs.message.arg.messageType = messageType;
			if (messageType === 'draft') {
				self.dialogs.message.arg.from = reply.document.arg.from;
				self.dialogs.message.arg.to = reply.document.arg.to;
				self.dialogs.message.arg.subject = reply.document.arg.subject;
				self.dialogs.message.arg.message = reply.document.arg.message;
				me.message.val(self.dialogs.message.arg.message);
				tiaacref.showElement(me.subject);
				tiaacref.hideElement(me.subjectRe);
				tiaacref.hideElement(me.replyMessage);
				tiaacref.showElement(me.topic.parent().parent());
			} else if (messageType === 'reply') {
				self.dialogs.message.arg.from = reply.document.arg.to;
				self.dialogs.message.arg.to = reply.document.arg.from;
				self.dialogs.message.arg.subject = (reply.document.arg.subject.substr(0,3) !== 'RE:' ? 'RE: ' : '') + reply.document.arg.subject;
				me.replyMessage.find('.input').html('<p>----------- Original Message  -----------</p>' + reply.document.arg.message);
				me.message.val('');
				tiaacref.hideElement(me.subject);
				tiaacref.showElement(me.subjectRe);
				tiaacref.showElement(me.replyMessage);
				tiaacref.hideElement(me.topic.parent().parent());
			} else if (messageType === 'new') {
				me.message.val('');
				me.subject.val('NONE');
				me.topic.val('NONE');
				tiaacref.showElement(me.topic.parent().parent());
				tiaacref.showElement(me.subject);
				tiaacref.hideElement(me.subjectRe);
				tiaacref.hideElement(me.replyMessage);
			}
			me.from.text(self.dialogs.message.arg.from);
			me.to.text(self.dialogs.message.arg.to);
			me.subject.val(self.dialogs.message.arg.subject);
			me.subjectRe.text(self.dialogs.message.arg.subject);
			me.primaryEmail.text(self.arg.data.primaryEmail);
			me.homePhone.text(self.arg.data.homePhone);
			me.businessPhone.text(self.arg.data.businessPhone);
			tcoipc.fire(self.event('analyticsDialog'), {dialog: self.dialogs.message, folder: self.activeeFolder})
			self.arg.data.alerts.message.close();
			self.dialogs.message.open();
		};
		self.dialogs.message.arg.process = function (type) {
			var folder,
				dialog = self.jq.dialogs.dlgMessage,
				messageType = self.dialogs.message.arg.messageType,
				today = new Date(),
				closeDialog = true;
			if (type === 'send') {
				if (messageType === 'new') {
					if (dialog.topic.val() == 'NONE' || dialog.subject.val() === "NONE" || dialog.message.val() === "") {
						closeDialog = false;
						self.arg.data.alerts.message.show();
					} else {
						folder = self.folderTypes['sent'];
						folder.addDocument({
							id: '#' + tiaacref.createId(), 
							to: dialog.to.text(),
							from: dialog.from.text(),
							subject: (self.dialogs.message.arg.reply ? dialog.subjectRe.text() : dialog.subject.val()),
							topic: dialog.topic.val(),
							message: dialog.message.val(),
							date: today,
							newDocument: true
						});
						tcoipc.fire(self.event('analyticsButton'), {button: 'send', folder: self.activeeFolder})
					}
				} else if (messageType === 'draft') {
					if (dialog.topic.val() == 'NONE' || dialog.subject.val() === "NONE" && dialog.message.val() === "") {
						closeDialog = false;
						self.arg.data.alerts.message.show();
					} else {
						this.reply.document.arg.subject = dialog.subject.val();
						this.reply.document.arg.topic = dialog.topic.val();
						this.reply.document.arg.message = dialog.message.val();
						this.reply.document.arg.date = today;
						self.moveDocuments('sent', this.reply);
						tcoipc.fire(self.event('analyticsButton'), {button: 'send', folder: self.activeeFolder})
					}
				} else if (messageType === 'reply') {
					if (dialog.message.val() === "") {
						closeDialog = false;
						self.arg.data.alerts.message.show();
					} else {
						folder = self.folderTypes['sent'];
						folder.addDocument({
							id: '#' + tiaacref.createId(), 
							to: dialog.to.text(),
							from: dialog.from.text(),
							subject: (self.dialogs.message.arg.reply ? dialog.subjectRe.text() : dialog.subject.val()),
							topic: dialog.topic.val(),
							message: dialog.message.val(),
							date: today,
							newDocument: true
						});
						tcoipc.fire(self.event('analyticsButton'), {button: 'send', folder: self.activeeFolder})
					}
				}
			} else if (type === 'draft') {
				if (messageType === 'new') {
					folder = self.folderTypes['draft'];
					folder.addDocument({
						id: '#' + tiaacref.createId(), 
						to: dialog.to.text(),
						from: dialog.from.text(),
						subject: dialog.subject.val(),
						topic: dialog.topic.val(),
						message: dialog.message.val(),
						date: today,
						newDocument: true
					});
				} else if (messageType === 'draft') {
					this.reply.document.arg.subject = dialog.subject.val();
					this.reply.document.arg.topic = dialog.topic.val();
					this.reply.document.arg.message = dialog.message.val();
					this.reply.document.arg.date = today;
					self.showActiveFolder();
					
				} else if (messageType === 'reply') {
					folder = self.folderTypes['draft'];
					folder.addDocument({
						id: '#' + tiaacref.createId(), 
						to: dialog.to.text(),
						from: dialog.from.text(),
						subject: dialog.subject.val(),
						topic: dialog.topic.val(),
						message: dialog.message.val(),
						date: today,
						newDocument: true
					});
				}
				tcoipc.fire(self.event('analyticsButton'), {button: 'draft', folder: self.activeeFolder})
			}
			if (closeDialog) {
				self.dialogs.message.close();
			}
			return false;
		}
	}
};

tiaacref.MessageManager.prototype = {
	event: function (eventName) {
		return tiaacref.eventName('MessageManager.' + eventName) + this.arg.id;
	},
	getFolder: function (folderName) {
		return this.folders[folderName];
	},
	getDocument: function (fodlerName, documentName) {
		var document = null,
			folder = this.getFolder(folderName);
		if (folder != null) {
			document = folder.getDocument(documentName);
		} 
		return document;
	},
	setSubjectsAndTopics: function (data) {
		var jqSubject = this.jq.dialogs.dlgMessage.subject,
			jqTopic = this.jq.dialogs.dlgMessage.topic,
			subject,
			si,
			sl,
			ti,
			tl,
			topic,
			option;
		data.unshift({value: "NONE", text: "-- Select One--", selected: true, topics:[]});
		this.subjectsAndTopics = {};
		jqSubject.empty();
		sl = data.length;
		for (si = 0; si < sl; si ++) {
			subject = data[si];
			option = '<option value="' + subject.value + '"' + (subject.selected ? ' selected="selected"' : '') + '>' + subject.text + '</option>';
			jqSubject.append(option);
			data[si].topics.unshift({value: "NONE", text: "-- Select One --", selected: true});
			this.subjectsAndTopics[subject.value] = data[si];
		}
		this.setTopic();
	},
	setTopic: function () {
		var jqSubject = this.jq.dialogs.dlgMessage.subject,
			jqTopic = this.jq.dialogs.dlgMessage.topic,
			topicValue = jqSubject.val(),
			topic,
			option,
			i,
			l;
		if (this.subjectsAndTopics[topicValue]) {
			jqTopic.empty();
			l = this.subjectsAndTopics[topicValue].topics.length;
			for (i = 0; i < l; i++) {
				topic = this.subjectsAndTopics[topicValue].topics[i];
				option = '<option value="' + topic.value + '"' + (topic.selected ? ' selected="selected"' : '') + '>' + topic.text + '</option>';
				jqTopic.append(option);
			}
		}
	},
	addFolder: function (folderArg) {
		var tcfId = '#tcf' + folderArg.id.substr(1);
		this.folders[folderArg.id] = new tiaacref.MessageFolder($.extend({parent: this}, folderArg));
		this.manager.folders[tcfId] = this.manager.addFolder($.extend({}, folderArg, {id: tcfId}));
		this.folders[folderArg.id].folder = this.manager.folders[tcfId]; 
		if (folderArg.isSelected) {
			this.activeFolder = this.folders[folderArg.id];
		}
		this.folderTypes[folderArg.type] = this.folders[folderArg.id];
		return this.folders[folderArg.id];
	},
	removeDocuments: function () {
		var i,
			document,
			documentList = {};
		for (i in this.activeFolder.documents) {
			document = this.activeFolder.documents[i];
			if (document.document.isSelected) {
				document.document.remove();
				document.jq.id.remove();
				delete this.activeFolder.documents[i];
				this.activeFolder.selectedDocuments --;
				this.activeFolder.numDocuments --;
				this.showToolbar();
				documentList[document.arg.id] = document;
			}
		}
		tcoipc.fire(this.event('documentRemoved'), {folder: this.activeFolder, documents: documentList});
	},
	moveDocuments: function (toFolderName, documentToMove) {
		var i,
			document,
			toFolder = null,
			documentList = {},
			moveType = toFolderName;
		if (toFolderName === 'restore') {
			toFolder = 'restore';
		} else {
			toFolder =  this.folderTypes[toFolderName];
		}
		if (documentToMove) {
			document = documentToMove;
			if (toFolderName === 'restore') {
				toFolder = document.arg.originalParent;
			}
			if (toFolder == null)  {
				moveType += " (" + document.arg.id + ")";
			} else {
				document.arg.originalParent = document.arg.parent;
				document.document.move(toFolder.folder);
				document.arg.parent = toFolder;
				toFolder.jq.table.children('tbody').append(document.jq.id);
				toFolder.documents[document.arg.id] = document;
				toFolder.numDocuments ++;
				delete this.activeFolder.documents[document.arg.id];
				if (document.document.isSelected) {
					this.activeFolder.selectedDocuments --;
				}
				this.activeFolder.numDocuments --;
				toFolder.documents[document.arg.id].select(false, false);
			}
			this.showActiveFolder();
			documentList[document.arg.id] = document;
		} else {
			for (i in this.activeFolder.documents) {
				document = this.activeFolder.documents[i];
				if (document.document.isSelected) {
					if (toFolderName === 'restore') {
						toFolder = document.arg.originalParent;
					}
					if (toFolder == null) {
						moveType += " (" + document.arg.id + ")";
					} else {
						document.arg.originalParent = document.arg.parent;
						document.document.move(toFolder.folder);
						document.arg.parent = toFolder;
						toFolder.jq.table.children('tbody').append(document.jq.id);
						toFolder.documents[i] = document;
						toFolder.numDocuments ++;
						delete this.activeFolder.documents[i];
						this.activeFolder.selectedDocuments --;
						this.activeFolder.numDocuments --;
						toFolder.documents[i].select(false, false);
					}
					this.showActiveFolder();
					documentList[document.arg.id] = document;
				}
			}
		}
		if (this.activeFolder.tableSort) {
			this.activeFolder.tableSort.reloadSortData();
		}
		if (toFolder){
			if (toFolder.tableSort) {
				toFolder.tableSort.reloadSortData();
			}
		}
		

		tcoipc.fire(this.event('documentMoved'), {fromFolder: this.activeFolder, toFolder: toFolder, documents: documentList, moveType: moveType});

	},
	clearFolderSelections: function () {
		var i,
			unreadCount,
			folder;
		for (i in this.folders) {
			folder = this.folders[i];
			unreadCount = folder.getUnreadCount();
			folder.jq.folderLink.removeClass('active');
			tiaacref.hideElement(folder.jq.folderLinkOpenImage);
			tiaacref.showElement(folder.jq.folderLinkClosedImage);
			tiaacref.hideElement(folder.jq.table);
			folder.jq.unreadCount.text(unreadCount);
			if (unreadCount === 0) {
				tiaacref.hideElement(folder.jq.unreadCount.parent());
			} else {
				tiaacref.showElement(folder.jq.unreadCount.parent());
			}
		}
	},
	showToolbar: function () {
		var i,
			l,
			folder = this.activeFolder,
			button;
		if (folder) {
			for (i in this.toolbar) {
				if (this.toolbar[i] != undefined) {
					tiaacref.hideElement(this.jq.buttons[i]);
				}
			}
		}
		l = folder.toolbars.length;
		for (i = 0; i < l; i++) {
			button = folder.toolbars[i];
			if (this.toolbar[button]) {
				tiaacref.showElement(this.jq.buttons[button]);
				if (folder.selectedDocuments === 0) {
					this.toolbar[button].disable();
				} else {
					this.toolbar[button].enable();
				}
			}
		}
	},
	showActiveFolder: function () {
		var i,
			l,
			folder = this.activeFolder,
			button;
		this.clearFolderSelections();
		if (folder) {
			folder.select();
			this.jq.folderNameHeader.html(folder.arg.name + " (<span class=\"unreadCount\">" + folder.getUnreadCount() + "</span>)");
			this.showToolbar();
		}
	}
};









tiaacref.defaults.MessageFolder = {
	eventName: {
		documentSelected: "documentSelected",
		documentRead: "documentRead",
		folderAdded: "folderAdded",
		folderSelectAll: "folderSelectAll"
	}
};
tiaacref.MessageFolder = function (arg) {
	var id = arg.id.substr(1),
		self = this;
	this.arg = arg;
	this.folder = null;
	this.documents = {};
	this.numDocuments = 0;
	this.selectedDocuments = 0;
	this.parentList = {};
	this.jq = {
		id: $(arg.id),
		table: $('#messageTable' + id),
		noFilesMessage: $('#messageTable' + id + " tr.noFilesMessage"),
		selectAll: $('#messagesSelectAll' + id),
		folderLink: $('#folderLink' + id),
		folderLinkClosedImage: $('#folderLinkClosedImage' + id),
		folderLinkOpenImage: $('#folderLinkOpenImage' + id),
		dataTemplate: $('#dataTemplate' + id),
		unreadCount: $(arg.id + ' span.unreadCount')
	};
	this.toolbars = this.jq.id.attr('data-toolbar').split(',');

	

	this.jq.id.on('click.MessageFolder.tiaacref', function (event) {
		event.preventDefault();
		event.stopPropagation();
		self.arg.parent.activeFolder = self;
		self.arg.parent.showActiveFolder();
	});
	this.jq.selectAll.on('click.MessageFolder.tiaacref', function (event) {
		var i,
			flag = $(this).prop('checked');
		if (self.numDocuments === 0) {
			$(this).prop('checked', false);
		} else {
			self.clearParent();
			for (i in self.documents) {
				if (self.documents[i].document.isSelected !== flag) {
					self.documents[i].select(flag);
					self.documents[i].jq.checkbox.prop('checked', flag);
					self.addParent(self.documents[i]);
				}
			}
			self.tellParent('folderSelectAll');
		}
	});
	
	this.jq.id.droppable({
		over: function (event, ui) {
			self.jq.id.addClass('dropping');
		},
		out: function (event, ui) {
			self.jq.id.removeClass('dropping');
		},
		drop: function (event, ui) {
			var fromType,
				toType = self.arg.type,
				allDocuments,
				i,
				l;
			if (ui.helper) {
				allDocuments = ui.helper.data('documents');
				if (allDocuments.length > 0) {
					fromType = allDocuments[0].arg.parent.arg.type;
					if (fromType !== toType && (
							(fromType === 'inbox' && (toType !== 'draft' && toType !== 'sent')) ||
							(fromType === 'draft' && (toType !== 'inbox' && toType !== 'sent')) || 
							(fromType === 'sent' && (toType !== 'inbox' && toType !== 'draft')) 
						)) {
						if (allDocuments.length > 1) {
							self.arg.parent.moveDocuments(toType);
						} else {
							self.arg.parent.moveDocuments(toType, allDocuments[0]);
						}
//						l = allDocuments.length;
//						for (i = 0; i < l; i ++) {
//							self.arg.parent.moveDocuments(toType, allDocuments[i]);
//						}
					}
				}
			}
			self.jq.id.removeClass('dropping');
		}

		
	});

	
	tcoipc.on(this.event('documentSelected'), function (document) {
		self.clearParent();
		self.selectedDocuments += (document.document.isSelected ? 1 : -1);
		self.addParent(document);
		self.tellParent('documentSelected');
	});
	tcoipc.on(this.event('documentRead'), function (document) {
		self.clearParent();
		self.addParent(document);
		self.tellParent('documentRead');
	});
	
	
	this.tableSort = new tiaacref.tableSort({
		id: '#messageTable' + id,
		multiSort: false,
		caseSensitive: true
	});


	
	
	

};
tiaacref.MessageFolder.prototype = {
	event: function (eventName) {
		return tiaacref.eventName('MessageFolder.' + eventName) + this.arg.id;
	},
	setFolder: function (folder) {
		this.folder = folder;
	},
	select: function () {
		var unreadCount = this.getUnreadCount();
		tiaacref.showElement(this.jq.table);
		if (this.numDocuments === 0) {
			tiaacref.showElement(this.jq.noFilesMessage);
		} else {
			tiaacref.hideElement(this.jq.noFilesMessage);
		}
		this.jq.folderLink.addClass('active');
		tiaacref.hideElement(this.jq.folderLinkClosedImage);
		tiaacref.showElement(this.jq.folderLinkOpenImage);
		this.jq.unreadCount.text(unreadCount);
		if (unreadCount === 0) {
			tiaacref.hideElement(this.jq.unreadCount.parent());
		} else {
			tiaacref.showElement(this.jq.unreadCount.parent());
		}
		this.setSelectAll(false);
	},
	redraw: function () {
		var tcdIdmod,
			document,
			i,
			tr,
			text,
			td,
			element,
			children = 0,
			tbody = this.jq.table.find('tbody');
		tbody.children('tr').not('.noFilesMessage').not('#dataTemplate' + this.arg.id.substr(1)).remove();
		for (i in this.documents) {
			document = this.documents[i];
			tcdIdmod = document.arg.id.substr(1);
			tr = this.jq.dataTemplate.clone(true);
			text = tr.attr('id');
			tr.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod});
			td = tr.children('td').first()
			element = td.find('img');
			text = element.attr('id');
			element.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod});
			td = td.next();
			element = td.find('label');
			text = element.attr('for');
			element.attr({'for': text.substring(0, text.indexOf('-')) + tcdIdmod});
			element = td.find('input');
			text = element.attr('id');
			element.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod});
			element = td.find('div');
			text = element.attr('id');
			element.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod}).text(document.arg.message);
			td = td.next();
			text = td.attr('id');
			td.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod, 'data-sortvalue': document.arg.to}).text(document.arg.to);
			td = td.next();
			td.attr({'data-sortvalue': document.arg.subject});
			element = td.find('a');
			text = element.attr('id');
			element.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod}).text(document.arg.subject);
			td = td.next();
			text = td.attr('id');
			td.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod, 'data-sortvalue': documentArg.date.toLocaleString()}).text(documentArg.date.toLocaleString());
			tr.addClass(children / 2 == Math.round(children / 2) ? "even" : "odd").addClass(document.document.isRead ? "" : "unread");
			tbody.append(tr);
			tiaacref.showElement(tr);
			children ++;
		}
		return;
	},
	addDocument: function (documentArg) {
		var tcdId = documentArg.id,
			tcdIdmod = tcdId.substr(1),
			tr,
			td,
			element,
			children,
			text;
		if (documentArg.newDocument) {
			tr = this.jq.dataTemplate.clone(true);
			text = tr.attr('id');
			tr.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod});
			td = tr.children('td').first()
			element = td.find('img');
			text = element.attr('id');
			element.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod});
			td = td.next();
			element = td.find('label');
			text = element.attr('for');
			element.attr({'for': text.substring(0, text.indexOf('-')) + tcdIdmod});
			element = td.find('input');
			text = element.attr('id');
			element.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod});
			element = td.find('div');
			text = element.attr('id');
			element.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod}).text(documentArg.message);
			td = td.next();
			text = td.attr('id');
			td.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod, 'data-sortvalue': documentArg.to}).text(documentArg.to);
			td = td.next();
			td.attr({'data-sortvalue': documentArg.subject});
			element = td.find('a');
			text = element.attr('id');
			element.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod}).text(documentArg.subject);
			td = td.next();
			text = td.attr('id');			
			td.attr({id: text.substring(0, text.indexOf('-')) + tcdIdmod, 'data-sortvalue': documentArg.date.toLocaleString()}).text(documentArg.date.toLocaleString());
			children = this.jq.table.find('tbody').children('tr').length;
			tr.addClass(children / 2 == Math.round(children / 2) ? "even" : "odd").addClass(documentArg.isRead ? "" : "unread");
			this.jq.table.find('tbody').append(tr);
			tiaacref.showElement(tr);
		}
		this.documents[documentArg.id] = new tiaacref.MessageDocument($.extend({parent: this}, documentArg));
		this.folder.documents[tcdId] = this.folder.addDocument($.extend({}, documentArg, {id: tcdId, message: this.documents[documentArg.id].arg.message}));
		this.documents[documentArg.id].document = this.folder.documents[tcdId];
		this.numDocuments ++;
		if (documentArg.newDocument) {
			this.clearParent();
			this.addParent(this.documents[documentArg.id]);
			this.tellParent('documentNew');
		}
		if (this.tableSort) {
			this.tableSort.reloadSortData();
		}
		return this.documents[documentArg.id];
	},
	getUnreadCount: function () {
		var i,
			count = 0;
		for (i in this.documents) {
			count += (this.documents[i].document.isRead ? 0 : 1);
		}
		return count;
	},
	setSelectAll: function (fireEvents) {
		var i;
		if (typeof fireEvents !== 'boolean') {
			fireEvents = true;
		}
		if (this.selectedDocuments === this.numDocuments && this.numDocuments !== 0) {
			this.jq.selectAll.prop('checked', true);
			if (fireEvents) {
				this.clearParent();
				for (i in this.documents) {
					this.addParent(this.documents[i]);
				}
				this.tellParent('folderSelectAll');
			}
		} else {
			if (this.jq.selectAll.prop('checked') && fireEvents) {
				this.clearParent();
				for (i in this.documents) {
					this.addParent(this.documents[i]);
				}
				this.tellParent('folderSelectAll');
			}
			this.jq.selectAll.prop('checked', false);
		}
	},
	setRead: function (flag) {
		var i;
		for (i in this.documents) {
			if (this.documents[i].document.isSelected) {
				this.documents[i].markAsRead(flag);
			}
		}
		if (this.arg.parent) {
			this.arg.parent.showActiveFolder();
		}
	},
	tellParent: function (event) {
		if (this.arg.parent) {
			tcoipc.fire(this.arg.parent.event(event), {folder: this, documents: this.parentList});
		}
	},
	clearParent: function () {
		this.parentList = {};
	},
	addParent: function (document) {
		this.parentList[document.arg.id] = document;
	}
	
};






















tiaacref.defaults.MessageDocument = {
	eventName: {
		documentSelected: "documentSelected",
		documentAdded: "documentAdded",
		documentRead: "documentRead"
	}
}
tiaacref.MessageDocument = function (arg) {
	var self = this,
		id = arg.id.substr(1),
		displayBox = null;
	this.arg = arg;
	this.document = null;
	this.arg.originalParent = null;
//	this.arg.originalParent = this.arg.parent;
	this.jq = {
		id: $(arg.id),
		checkbox: $('#messageCheckbox' + id),
		messageSubject: $('#messageSubject' + id),
		messageDisplay: $(arg.id + ' [data-messagedisplay="true"]'),
		icon: $('#messageIcon' + id)
	};
	this.arg.message = $('#messageMessage' + id).html();
	$('#messageMessage' + id).remove();
	
	this.jq.checkbox.off('click.MessageDocument.tiaacref').on('click.MessageDocument.tiaacref', function (event) {
		self.select($(this).prop('checked'));
		if (self.arg.parent) {
			self.arg.parent.setSelectAll();
		}
	});
	this.arg.draggableElement = $('<div />').addClass("dragging").css('opacity', 0.8).text(this.jq.messageSubject.text());
	this.arg.draggableElement.data('document', this);
	
	this.jq.icon.draggable({
		revert: true,
		revertDuration: 50,
		helper: function () {
			var documents = [],
				allDocuments;
			if (self.arg.parent.selectedDocuments > 1 && self.document.isSelected) {
				self.arg.draggableElement.text('Moving ' + self.arg.parent.selectedDocuments + ' items');
				allDocuments = self.arg.parent.documents;
				for (var i in allDocuments) {
					if (allDocuments[i].document.isSelected) {
						documents.push(allDocuments[i]);
					}
				}
			} else {
				documents.push(self);
				self.arg.draggableElement.text(self.jq.messageSubject.text());
			}
			self.arg.draggableElement.data('documents', documents);
			return self.arg.draggableElement; 
		}
	});
	
//	this.jq.messageSubject.on('click.MessageDocument.tiaacref', function (event) {
	this.jq.messageDisplay.on('click.MessageDocument.tiaacref', function (event) {
		event.preventDefault();
		event.stopPropagation();
		if (!self.arg.parent) {
			self.arg.parent.arg.parent.dialogs.read.arg.loadAndOpen(self);
		} else if (self.arg.parent.arg.type !== 'draft') {
			self.arg.parent.arg.parent.dialogs.read.arg.loadAndOpen(self);
		} else {
			self.arg.parent.arg.parent.dialogs.message.arg.loadAndOpen(self, 'draft');
		}
	});
};
tiaacref.MessageDocument.prototype = {
	event: function (eventName) {
		return tiaacref.eventName('MessageDocument.' + eventName) + this.arg.id;
	},
	select: function (flag, fireEvent) {
		if (typeof fireEvent !== 'boolean') {
			fireEvent = true;
		}
		this.document.select(flag, fireEvent);
		if (flag) {
			this.jq.id.addClass('rowHighlight').attr('aria-selected', true);
		} else {
			this.jq.id.removeClass('rowHighlight').removeAttr('aria-selected');
		}
		this.jq.checkbox.prop('checked', flag);
		if (fireEvent) {
			this.tellParent('documentSelected');
		}
	},
	markAsRead: function (flag) {
		if (this.document.isRead !== flag) {
			this.document.markAsRead(flag);
			if (flag) {
				this.jq.id.removeClass('unread');
			} else {
				this.jq.id.addClass('unread');
			}
			this.tellParent('documentRead');
		}
	}, 
	tellParent: function (event) {
		if (this.arg.parent) {
			tcoipc.fire(this.arg.parent.event(event), this);
		}
	}
};