<template>
  <v-card 
  class="mx-auto"
  width="95%" 
  height="95%"
  elevation="6"
  shaped
  id="login">
  <form class="form-signin" @submit.prevent="login">
    <v-card-title  class="h3 mb-3 font-weight-normal">Please Sign In</v-card-title>
      <div class="alert alert-danger" role="alert" v-if="invalidCredentials">
        Invalid username and password!
      </div>
      <div class="alert alert-success" role="alert" v-if="this.$route.query.registration">
        Thank you for registering, please sign in.
      </div>
   <v-text-field
      v-model="user.username"
      label="UserName"
      required
    
    ></v-text-field>
    <v-text-field
      v-model="user.password"
      label="Password"
      type="password"
      required
 
    ></v-text-field>
    <p>
    <router-link :to="{name: 'register' }">Register Account? </router-link>
    </p>
   <v-btn class="mr-4" @click="login" type="submit">Sign In</v-btn>
   <v-btn @click="clear" type="reset">Clear</v-btn>
 </form>
   </v-card>
</template>



<script>
import auth from '../auth';

export default{
  name: 'login',
  components: {},
  data() {
    return {
      user: {
        username: '',
        password: ''
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/login`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.user),
      })
        .then((response) => {
          if (response.ok) {
            return response.text();
          } else {
            this.invalidCredentials = true;
          }
        })
        .then((token) => {
          if (token != undefined) {
            if (token.includes('"')) {
              token = token.replace(/"/g, '');
            }
            auth.saveToken(token);
            this.$router.push('/');
          }
        })
        .catch((err) => console.error(err));
    },
    clear() {
        this.username = ''
        this.password= ''
        this.rest()
      },
  },
  computed: {
     usernameErrors () {
        const errors = []
        if (!this.$v.username.$dirty) return errors
        !this.$v.username.maxLength && errors.push('Username must be at most 20 characters long')
        !this.$v.username.required && errors.push('Username is required.')
        return errors
      },
      passwordErrors () {
        const errors = []
        if (!this.$v.password.$dirty) return errors
        !this.$v.password.required && errors.push('Password is required')
        return errors
      }
  }
};
</script>

<style>
    .form-signin {
     padding: 10px;
     padding-left: 15%;
      width: 60%;
    }
</style>
