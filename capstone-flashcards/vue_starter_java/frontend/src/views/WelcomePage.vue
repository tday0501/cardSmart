<template>
  <v-parallax src="../assets/cardsmartbackground.jpeg" style="height: 100%">
    <v-content>
      <v-col class="text-center" cols="12">
        <h1>Welcome To Card Smart!</h1>
        <p>
          Card Smart is a flashcard application service. Where you can create a deck of flashcards for specific subjects you are studying.
          With Card Smart you will then be able to create tests and even share flashcard decks with others. Feel
          free to create your free profile and connect with others in our online learning community.
        </p>
      </v-col>
      <v-card>
        <v-card-title>Example Decks and Cards</v-card-title>
        <v-container>
        <v-card
          color="blue lighten-2"
          width="300"
          height="150"
          elevation="10"
          class="white--text mx-auto"
        >
          <v-card-title>Deck Name</v-card-title>
          <v-card-subtitle>Deck Description</v-card-subtitle>
        </v-card>
        <v-divider></v-divider>
        <v-card
          class="mx-auto"
          width="500"
          height="200"
          elevation="15"
        >
          <v-card-title>Card Title</v-card-title>
          <v-card-text>Card Question</v-card-text>
        </v-card>
        </v-container>
      </v-card>
    </v-content>
  </v-parallax>
</template>

<script>

export default {
  name: "welcomepage",
  components: {
  }
};
</script>

<style>
v-parallax {
  height: 100%;
}
</style>