<template>
  <v-app style="
  background: #FFFFFF;">
   <v-app-bar app color="green lighten-1" elevate-on-scroll>
      <div class="d-flex align-center">
        <v-img
          alt="Vuetify Logo"
          class="shrink mr-2"
          contain
          src="../src/assets/cardSmartLogo.png"
          transition="scale-transition"
          width="200"
        />
      </div>
      <v-spacer></v-spacer>
      <v-btn v-if="isLoggedIn() === false" href="/Login" text>Sign In</v-btn>

      <v-btn v-if="isLoggedIn() === true" text @click="logout">
        <span class="mr-2">
          <button>Logout</button>
        </span>
        <v-icon>mdi-open-in-new</v-icon>
      </v-btn>
    </v-app-bar>
    <v-content>
      <router-view />
    </v-content>
  </v-app>
</template>

<script>
import auth from './auth.js'

export default {
  name: 'App',

  data() {
    return {
    }
  },
  methods: {
    logout() {
      
      auth.logout();     
      this.$router.push('/welcomepage');
    },
    isLoggedIn() {
     if (auth.getUser() === null){
       return false;
     } else {
       return true;
     }
    }
  }, 
  computed: {
    theme(){
      return (this.$vuetify.theme.dark) ? 'dark' : 'light'
    },

  }
};
</script>
<style>
v.application {
  height: 100vh;
}
</style>
