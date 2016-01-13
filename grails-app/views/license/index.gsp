<html>
   <head>
      <title>License Management Plugin</title>
      <meta name="layout" content="main" />
   </head>
   <body>
    <div  class="body">
      <h1>License Details</h1>
      <div class="dialog">
       <g:if test="${license}">
        <table>
          <tr class='prop'>
            <td valign='top' class='name'>Subject:</td>
            <td valign='top' class='value'>${license?.subject}</td>
          </tr>
          <tr class='prop'>
            <td valign='top' class='name'>License holder:</td>
            <td valign='top' class='value'>${license?.holder}</td>
          </tr>
          <tr class='prop'>
            <td valign='top' class='name'>Issuer:</td>
            <td valign='top' class='value'>${license?.issuer}</td>
          </tr>
          <tr class='prop'>
            <td valign='top' class='name'>Issued:</td>
            <td valign='top' class='value'>${license?.issued}</td>
          </tr>
          <tr class='prop'>
            <td valign='top' class='name'>Valid not after:</td>
            <td valign='top' class='value'>${license?.notAfter}</td>
          </tr>
          <tr class='prop'>
            <td valign='top' class='name'>Valid not before:</td>
            <td valign='top' class='value'>${license?.notBefore}</td>
          </tr>
          <tr class='prop'>
            <td valign='top' class='name'>Consumer type:</td>
            <td valign='top' class='value'>${license?.consumerType}</td>
          </tr>
          <tr class='prop'>
            <td valign='top' class='name'># Consumer:</td>
            <td valign='top' class='value'>${license?.consumerAmount}</td>
          </tr>
          <tr class='prop'>
            <td valign='top' class='name'>Info:</td>
            <td valign='top' class='value'>${license?.info}</td>
          </tr>
        </table>
       </g:if>
       <g:else>
         <b>License expired or invalid</b>
       </g:else>
      </div>

      <p>&nbsp;</p>

      <g:uploadForm action="install">
         <b>Install new license:</b>
         <input type="file" name="licenseFile">
         <input type="submit" value="Submit">
      </g:uploadForm>
    </div>
   </body>
</html>
