jQuery.fn.selectBox=function(o){return this.each(function(){var oThis=this;var oSelected=null;var state='closed';var iSelectedIndex=0;var oSelectOffset=jQuery(this).offset('HTML');jQuery(this).wrap('<div></div>').css({top:'-1000px',left:'-1000px',position:'absolute'})
var oContainer=jQuery(this).parent().addClass(o.css);oContainer.append('<div><p></p></div><ul></ul>');jQuery("div",oContainer).css({width:oSelectOffset.width+'px'}).toggle(function(){jQuery("ul",oContainer).slideDown('fast');jQuery("li",oContainer).removeClass('active');if(oSelected==null){jQuery("li:eq(0)",oContainer).addClass('active');oSelected=jQuery("li:eq(0)",oContainer);}else{oSelected.addClass('active');}
state='opened';jQuery(oThis)[0].focus();},function(){jQuery("ul",oContainer).slideUp('fast');state='closed';jQuery(oThis)[0].blur();}).mouseover(function(){jQuery(oThis)[0].focus();}).mouseout(function(){jQuery(oThis)[0].blur();});jQuery().click(function(){if(state=='opened'){jQuery("div",oContainer).trigger("click");}});jQuery(this).keyup(function(){setValue();});jQuery('option',this).each(function(i){var o=this;jQuery(this).click(function(){this.selected=true;});jQuery("ul",oContainer).append('<li class="geeko">'+jQuery(this).text()+'</li>');jQuery("li:eq("+i+")",oContainer).click(function(){jQuery(o).click();jQuery("div",oContainer).trigger("click");$("#search form input.textfield").attr('value',jQuery(o).text());var newClasses=$(this).attr('class');$("#search form div.select p").attr('class',newClasses);oSelected=$(this)}).mouseover(function(){jQuery(this).addClass('active');jQuery(oThis)[0].focus();}).mouseout(function(){jQuery(this).removeClass('active');jQuery(oThis)[0].blur();});});jQuery("ul",oContainer).hide();var positionListElement=function(){var oOffset=jQuery("div",oContainer).offset('HTML');jQuery("ul",oContainer).css({left:oOffset.left+'px',top:oOffset.top+parseInt(oOffset.height)+'px',position:'absolute',width:oOffset.width+'px'});};var setValue=function(){var val=jQuery(":selected",oThis).text();jQuery("div p",oContainer).html(val);oSelected=jQuery("li:eq("+jQuery(oThis)[0].selectedIndex+")",oContainer);jQuery("li",oContainer).removeClass('active');oSelected.addClass('active');};setValue();positionListElement();jQuery(window).resize(positionListElement);});};