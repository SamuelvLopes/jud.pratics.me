<!-- eslint-disable vue/multi-word-component-names -->
<template>
    <v-container>
      <v-row class="justify-center">
        <v-col cols="12" md="6">
          <v-card>
            <v-card-title class="headline">Login</v-card-title>
            <v-card-text>
              <v-form v-model="valid" ref="form">
                <v-text-field
                  v-model="email"
                  label="E-mail"
                  type="email"
                  :rules="emailRules"
                  required
                />
                <v-text-field
                  v-model="password"
                  label="Senha"
                  type="password"
                  :rules="passwordRules"
                  required
                />
                <v-btn :disabled="!valid" color="primary" @click="submitLogin">
                  Entrar
                </v-btn>
                <!-- Botão de redirecionamento para o Cadastro -->
                <v-btn text @click="goToRegister">
                  Não tem uma conta? Registre-se
                </v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
  export default {
    data() {
      return {
        email: '',
        password: '',
        valid: false,
        emailRules: [
          v => !!v || 'E-mail é obrigatório',
          v => /.+@.+\..+/.test(v) || 'E-mail deve ser válido'
        ],
        passwordRules: [
          v => !!v || 'Senha é obrigatória',
          v => v.length >= 6 || 'A senha deve ter pelo menos 6 caracteres'
        ]
      };
    },
    methods: {
      submitLogin() {
        // Lógica de autenticação (backend integration)
        console.log('Login com e-mail:', this.email);
        this.$router.push('/home');
      },
      goToRegister() {
        // Redireciona para a página de cadastro
        this.$router.push('/register');
      }
    }
  };
  </script>
  
  <style scoped>
  /* Estilos personalizados */
  </style>
  