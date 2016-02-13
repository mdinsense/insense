var TIAA = TIAA || {};

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
		},
		setPos : function($menu, align){
			var menuLeftPos = $menu.offset().left,
				menuWidth = menuLeftPos + $menu.outerWidth();
			$menu.find('.megamenu').css({'visibility' : 'hidden', 'display' : 'block'}).each(function(i,mega){
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

// value of 12 introduced to adjust for space and icon in "full" menus 2014-02-28

			var $menu = $menu,
				//menuWidth = $menu.width(),
				menuWidth = $menu.width() - 12,
				liWidth = 0,
				widthDiff = 0;

			$menu.children('li').each(function(i,li){
				liWidth += $(li).outerWidth();
			});

			//widthDiff = parseInt((menuWidth - liWidth) / $menu.children('li').length);
			widthDiff = parseInt((menuWidth - liWidth - 12) / $menu.children('li').length);

			$menu.children('li').each(function(i,li){
				var newWidth = $(li).width() + widthDiff;

				if (i == $menu.children('li').length-1){
					if (widthDiff*$menu.children('li').length < (menuWidth - liWidth)){
						//newWidth = newWidth + ((menuWidth - liWidth) - (widthDiff*$menu.children('li').length));
						newWidth = newWidth + ((menuWidth - liWidth) - (widthDiff*$menu.children('li').length)) + 12;
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

jQuery(document).ready(function() {"use strict";
TIAA.MegaMenu.init($('ul.l1nav'),'left',true);
});
