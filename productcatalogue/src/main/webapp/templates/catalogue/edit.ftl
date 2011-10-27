[#ftl]
[#include "../layout.ftl"]
[@page]

[#assign priceCounter = 1]
[#macro renderPrice currency unitPrice salePrice]
	<select name="price[currency,${priceCounter}]">
		[#list ["","GBP","EUR","USD","YEN"] as c]
			<option value="${c}" [#if c==currency] selected="selected" [/#if] >${c}</option>
		[/#list]
	</select>
	<label>UnitPrice</label>
	<input type="text" name="price[unitPrice,${priceCounter}]" value="${unitPrice}" class="unitPrice"/>
	<label>SalePrice</label>
	<input type="text" name="price[salePrice,${priceCounter}]" value="${salePrice}" class="salePrice"/>
	
	[#assign priceCounter = priceCounter + 1]
[/#macro]

<h3>Edit Product</h3>

[#if saved]
<p class="msg">
	Product saved. <a href="../list">Back to the list of products.</a>
</p>
[/#if]
<form method="post">
	<label>Title</label>
	<input name="title" value="${product.title}"/>
	<br/>
	<label>Description</label>
	<textarea rows="10" cols="80" name="description">${product.description}</textarea>
	<br/>
	[#----------------------------------------------------------------------------- 
		product prices 
	-------------------------------------------------------------------------------]
	<fieldset>
		<legend>Prices</legend>
		[#list product.prices as price]
			[@renderPrice currency="${price.currency}" unitPrice="${price.unitPrice}" salePrice="${price.salePrice}" /]
			<br/>
		[/#list]
		[#-- provide 3 extra slots for the user to enter prices in new currencies --]
		[#list 1..3 as i]
			[@renderPrice currency="" unitPrice="" salePrice="" /]
			<br/>
		[/#list]
	</fieldset>
	
	[#----------------------------------------------------------------------------- 
		product attributes 
	-------------------------------------------------------------------------------]
	<fieldset>
		<legend>Attributes</legend>
	</fieldset>
	<input type="submit" value="Save"/>
	<a href="../list">Cancel</a>
</form>
[/@page]