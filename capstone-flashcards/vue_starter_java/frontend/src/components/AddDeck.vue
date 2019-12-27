<template>
<div>
<v-card class="mx-auto" max-width="400">
    <v-form>   
        <v-container>
            <v-text-field
            v-model="deckName"
            label="Deck Name"
            clearable
            required></v-text-field>
            <v-switch v-model="isPublic" class="ma-2" label="Public"></v-switch>
            <v-btn @click="addNewDeck" width="150" absolute bottom right small color="green lighten-1">Submit</v-btn>
        </v-container>
    </v-form>
</v-card>
</div>
</template>

<script>
import auth from '@/auth.js'

export default {
    data() {
        return {
            subjectId: '',
            deckName: '',
            isPublic: ''
        }
    }, 
    methods: {
        addNewDeck () {
      fetch(process.env.VUE_APP_REMOTE_API +  '/api/decks', {
                   headers: {Authorization: 'Bearer ' + auth.getToken(), 'Content-Type': 'application/json'},
        method: 'POST',
        body: JSON.stringify({subjectId: this.subjectId, deckName: this.deckName, isPublic: this.isPublic})
      })
      .then(() => this.$router.push('/')).then(() => this.$emit("deckSubmitted"));
      },
    }

}
</script>

<style>

</style>