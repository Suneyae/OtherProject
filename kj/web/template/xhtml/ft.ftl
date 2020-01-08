<fieldset class="fieldset3" align="${parameters.align}"  style="cursor:default;width:${parameters.width}" >
	  <legend class="legend" style="cursor:hand;"> 
		  <span onclick="FWshowTable('img_${parameters.id}','div_${parameters.id}')">
		    <img id="img_${parameters.id}" src="${parameters.ctx}/themes/default/images/query_icon_right.gif">
		  </span> 
	      <span title="${parameters.title}" onclick="FWshowTable('img_${parameters.id}','div_${parameters.id}')">${parameters.header}</span> 
	  </legend>
	  <div id="div_${parameters.id}" >
	      ${parameters.body!""}
	   </div>
 </fieldset>