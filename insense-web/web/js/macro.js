var mintJs = function () {
	"use strict";
};
mintJs.defaults = {
		cookie: {
		},
		interPortletCommunications: {
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
			showPerPageDropDown: false,
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
		}
}
mintJs.paginate = function (arg) {
	"use strict";
	var defaults = mintJs.defaults.paginate,
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
		throw mintJs.error('', 'arg.perPage is equal to or less than 0 for paginate.');
	}
	jq = ({id: $(arg.id), table: $(arg.table)});
	if (jq.id.length === 0) {
		throw mintJs.error('badId', arg.id, 'paginate');
	}
	if (jq.table.length === 0) {
		throw mintJs.error('badId', arg.table, 'paginate');
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
		tcoipc.on(mintJs.eventName('paginate.drawTable') + arg.table, function(data) {
			self.draw();
		});
		tcoipc.on(mintJs.eventName('paginate.drawTable') + arg.id, function(data) {
			self.draw();
		});
		tcoipc.on(mintJs.eventName('paginate.drawPagination') + arg.id, function(data) {
			self.drawPagination();
		});
	}
	tcoipc.on(mintJs.eventName('paginate.drawPagination') + arg.table, function(data) {
		self.drawPagination();
	});
	tcoipc.on(mintJs.eventName('paginate.recalcDataSource') + arg.table, function(data) {
		self.recalcDataSource();
	});
	tcoipc.on(mintJs.eventName('paginate.setDataSource') + arg.table, function(data) {
		self.setDataSource(data);
	});
	// set up num records listener. Even if autoCount is true, this is setup in case we need to change 
	// the number of records later.
	self = this;
	tcoipc.on(this.getNumRecordsEventName(), function (num) {
		self.numRecords = num;
	});
	tcoipc.on(mintJs.eventName('paginate.setTableNumRecords') + arg.table, function (num) {
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
var tcoipc = new mintJs.interPortletCommunications();

mintJs.interPortletCommunications = function () {
	"use strict";
	this.events = {};
	this.eventCount = 0;
};