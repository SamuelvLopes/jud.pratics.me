<!-- eslint-disable vue/multi-word-component-names -->
<template>
    <v-app>
      <!-- Navbar -->
      <v-app-bar app>
        <v-toolbar-title>JUD Pratics</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn text @click="goToHome">Home</v-btn>
        <v-btn text @click="goToProfile">Perfil</v-btn>
        <v-btn text @click="logout">Sair</v-btn>
      </v-app-bar>
  
      <!-- Conteúdo principal -->
      <v-main>
        <v-container>
          <h2>Bem-vindo ao JUD Pratics!</h2>
          <p>Conecte-se com advogados ou gerencie seu perfil jurídico.</p>
        </v-container>
      </v-main>
    </v-app>
  </template>
  
  <script>
  export default {
    methods: {
      goToHome() {
        this.$router.push('/home');
      },
      goToProfile() {
        this.$router.push('/profile');
      },
      logout() {
        // Lógica de logout
        console.log('Usuário desconectado');
        this.$router.push('/login');
      }
    }
  };
  </script>
  
  <style scoped>
  /* Estilos personalizados */
  </style>
  