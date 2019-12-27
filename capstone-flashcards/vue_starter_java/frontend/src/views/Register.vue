<template>
  <v-card 
  class="mx-auto"
  width="95%" 
  height="95%"
  elevation="6"
  shaped
  id="register">
  <form class="form-register"  @submit.prevent="register">
     <v-card-title class="h3 mb-3 font-weight-normal">Create Account</v-card-title>
      <div class="alert alert-danger" role="alert" v-if="registrationErrors">
        There were problems registering this user.
      </div>
    <v-text-field
      v-model="user.username"
      id="username"
     
      label="UserName"
      required
    ></v-text-field>
    <v-text-field
      v-model="user.password"
      id="password"
      label="Password"
      type="password"
      required
    ></v-text-field>
    <v-text-field
    v-model="user.confirmPassword"
    id="confirmPassword"
    label="Confirm Password"
    type="password"
    required
    ></v-text-field>
    <p>
    <router-link :to="{ name: 'login' }">
        Have an account?
      </router-link>
    </p>
    <v-btn class="mr-4" @click="register" type="submit">Create Account</v-btn>
    <v-btn @click="clear">Clear</v-btn>
  </form>
  </v-card>
</template>

<script>
export default {
  name: 'register',
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
    };
  },
  methods: {
    register() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/register`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        },
        body: JSON.stringify(this.user),
      })
        .then((response) => {
          if (response.ok) {
            return (response.json());
          } else {
            this.registrationErrors = true;
            throw 'Register returned: ' + response.status;
          }
        })
        .then ((parsedData) => {
            if (parsedData.success) {
              this.$router.push({ path: '/login', query: { registration: 'success' } });
            } else {
              this.registrationErrors = true;
            }
        })
        .catch((err) => console.log(err));
    },
    clear() {
        this.$v.$reset()
        this.username = ''
        this.password= ''
        this.confirmPassword= ''
      },
  },
};
</script>

<style>
  .form-register {
     padding: 10px;
     padding-left: 15%;
      width: 60%;
    }
</style>
