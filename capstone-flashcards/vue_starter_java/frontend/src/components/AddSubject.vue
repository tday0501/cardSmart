<template>
  <div>
    <v-card class="mx-auto" max-width="400">
      <v-list>
        <v-form>
          <v-list-item>
            <v-text-field
              class="text-field"
              v-model="subjectName"
              label="Please Enter Subject"
              clearable
            ></v-text-field>
          </v-list-item>
          <v-list-item v-show="!selectEmoji">Select Icon Below:</v-list-item>
          <v-alert v-show="selectEmoji" class="alert">Please select an emoji:</v-alert>
          <v-list class="emoji-card">
            <v-icon
              class="text-field emoji-item"
              v-for="(emoji,index) in emojis"
              :key="index"
              @click="addEmoji(emoji)"
              required
              clearable
            >{{emoji}}</v-icon>
          </v-list>
        </v-form>
        <v-list-item>
          <v-btn
            width="150"
            absolute
            top
            right
            small
            color="green lighten-1"
            @click="addSubject"
          >Submit</v-btn>
        </v-list-item>
      </v-list>
    </v-card>
  </div>
</template>


<script>
import auth from "@/auth.js";
import Emoji from "node-emoji";

export default {
  data() {
    return {
      emojiSelected: "",
      subjectName: "",
      emojis: [],
      selectEmoji: false,
      enterSubject: false
    };
  },
  methods: {
    addSubject: function() {
      if (this.emojiSelected === "") {
        this.selectEmoji = true;
      } else if (this.subjectName === ""){
        this.enterSubject = true
      } else {
        fetch(process.env.VUE_APP_REMOTE_API + "/api/subjects", {
          headers: {
            Authorization: "Bearer " + auth.getToken(),
            "Content-Type": "application/json"
          },
          method: "POST",
          body: JSON.stringify({
            subjectName: this.subjectName,
            iconPath: this.emojiSelected
          })
        })
          .then(() => this.$router.push("/"))
          .then(() => this.$emit("subjectSubmitted"))
          .then(() => {
            this.selectEmoji = false;
            this.enterSubject = false;
            this.subjectName = ''
          });
      }
    },
    addEmoji: function(emoji) {
      this.emojiSelected = emoji;
    }
  },
  created() {
    this.emojis = Object.values(Emoji.emoji);
  }
};
</script>

<style>
.text-field {
  display: inline-block;
}
.emoji-card {
  max-height: 20em;
  overflow-y: scroll;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}
.emoji-item {
  display: flex;
  justify-content: space-evenly;
  margin: 0.7em;
}
.alert{
  color: red !important
}
.active{
  background-color: green
}
div {
  overflow: hidden;
}
</style>