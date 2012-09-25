<!-- 
 This file is part of SmartLib Project.

    SmartLib is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SmartLib is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SmartLib.  If not, see <http://www.gnu.org/licenses/>.
    
    
	Author: Paschalis Mpeis

	Affiliation:
	Data Management Systems Laboratory 
	Dept. of Computer Science 
	University of Cyprus 
	P.O. Box 20537 
	1678 Nicosia, CYPRUS 
	Web: http://dmsl.cs.ucy.ac.cy/
	Email: dmsl@cs.ucy.ac.cy
	Tel: +357-22-892755
	Fax: +357-22-892701
    
    
    -->

<!--TODO FIX BUG-->
    <div id="login-panel">
        <input type="text" name="username" placeholder="Username"
               autocomplete="off"
               autofocus="autofocus"
               id="loginUsernameField"
               onkeyup="toggleLoginButton()"
                />

        <input type="password" name="password" placeholder="Password"

               onkeyup="toggleLoginButtonPassword(event)"
               id="loginPasswordField"
               autocomplete="off"  />
        <!--        Login Button-->
        <div  id="login-panel-submit">Login</div>
    </div>

<!--  OLD CODE  -->
<!--    <form id="login-widget" action="mobile/authenticate.php" method="post" -->
<!--	enctype="multipart/form-data">-->
<!--	<fieldset>-->
<!---->
<!--        Username:<br/>-->
<!--		<div >-->
<!--			<input type="text" name="username" value="Type your Username"   class="loginhint"-->
<!--				onBlur="if(this.value=='') {-->
<!--                this.value='Type your Username';-->
<!--                this.className='loginhint';-->
<!--                }-->
<!--                -->
<!--                "-->
<!--				onFocus="if(this.value =='Type your Username' ){-->
<!--                 this.value='';-->
<!--                 this.className='';-->
<!--                 }-->
<!--                 " /> -->
<!--		</div>-->
<!--  		<br/>-->
<!--        Password:<br/>-->
<!--        <div >-->
<!--			<input type="text" name="password" value="Type your Password" class="loginhint"-->
<!--				onBlur="if(this.value=='') {-->
<!--                this.value='Type your Password';-->
<!--                type='text';-->
<!--                this.className='loginhint';-->
<!--                }-->
<!--                "-->
<!--				onFocus="if(this.value =='Type your Password' ){-->
<!--                this.value='';-->
<!--               	type='password';-->
<!--                this.className='';-->
<!--                }-->
<!--                "-->
<!--                 /><a class="textHandle"-->
<!--				href="#" onClick="document.getElementById('login-widget').submit()">Login</a>-->
<!--		</div>-->
<!--	</fieldset>-->
<!--</form>-->
