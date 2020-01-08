<a href="http://weibo.com/daxixis/home?wvr=5&sudaref=www.bing.com">${parameters.message?html}</a>  
<title>Welcome wyl</title><br>


<h1>weee</h1>


<#settion number_format="currency"/> 
<#assign answer=42/> 
<br>
wei下
<br>
${answer}
<br> 
${answer?string} <#-- the same as ${answer} --> 
<br>
${answer?string.number} 
<br>
${answer?string.currency} 
<br>
${answer?string.percent} 
<br>
${answer} 
<br>
-------
<br>
<#assign x=2.582/> 
<#assign y=4/> 
#{x; M2} <#-- 输出2.58 --> 
#{y;M3}
------------------
