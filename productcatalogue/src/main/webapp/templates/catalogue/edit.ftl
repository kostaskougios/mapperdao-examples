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

[#assign attrCounter = 1]
[#macro renderAttribute name value]
	<label>Name</label>
	<input type="text" name="attribute[name,${attrCounter}]" value="${name}" class="attributeName"/>
	<label>Value</label>
	<input type="text" name="attribute[value,${attrCounter}]" value="${value}" class="attributeValue"/>
	[#assign attrCounter = attrCounter + 1]
	<div class="clear"></div>
[/#macro]

[#assign catCounter = 1]
[#macro renderCategory hierarchy]
	<label>Category hierarchy</label>
	<input type="text" name="category[hierarchy,${catCounter}]" value="${hierarchy}" class="categoryHierarchy"/>
	
	[#assign catCounter = catCounter + 1]
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
	<fieldset id="prices">
		<legend>Prices</legend>
		[#list product.prices as price]
			[@renderPrice currency="${price.currency}" unitPrice="${price.unitPrice}" salePrice="${price.salePrice}" /]
		[/#list]
		[#-- provide 3 extra slots for the user to enter prices in new currencies --]
		[#list 1..3 as i]
			[@renderPrice currency="" unitPrice="" salePrice="" /]
		[/#list]
	</fieldset>
	
	[#----------------------------------------------------------------------------- 
		product attributes 
	-------------------------------------------------------------------------------]
	<fieldset id="attributes">
		<legend>Attributes</legend>
		[#list product.attributes as attribute]
			[@renderAttribute name="${attribute.name}" value="${attribute.value}"/]
		[/#list]
		[#list 1..3 as i]
			[@renderAttribute name="" value=""/]
		[/#list]
	</fieldset>
	[#----------------------------------------------------------------------------- 
		product categories 
	-------------------------------------------------------------------------------]
	<fieldset id="categories">
		<legend>Categories</legend>
		[#list product.categories as category]
			[@renderCategory hierarchy="${category.commaSeparated}"/]
		[/#list]
		[#list 1..3 as i]
			[@renderCategory hierarchy=""/]
		[/#list]
	</fieldset>
	<input type="submit" value="Save"/>
	<a href="../list">Cancel</a>
</form>
[/@page]