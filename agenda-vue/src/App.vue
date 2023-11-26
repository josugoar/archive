<script lang="ts">
import { contacts, deleteContact } from './firebase'

export default {
  data() {
    return { contacts }
  },
  methods: { deleteContact },
  async mounted() {
    // @ts-ignore
    await import('/js/jquery.js')
    // @ts-ignore
    await import('/js/bootstrap.min.js')
    // @ts-ignore
    await import('https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js')
    // @ts-ignore
    await import('/js/classie.js')
    // @ts-ignore
    await import('/js/cbpAnimatedHeader.js')
    // @ts-ignore
    await import('/js/freelancer.js')
    // @ts-ignore
    await import('/js/jqBootstrapValidation.js')
    // @ts-ignore
    await import('/js/contact_me.js')
  }
}
</script>

<template>
  <!-- Navigation -->
  <nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
      <!-- Brand and toggle get grouped for better mobile display -->
      <div class="navbar-header page-scroll">
        <button
          type="button"
          class="navbar-toggle"
          data-toggle="collapse"
          data-target="#bs-example-navbar-collapse-1"
        >
          <span class="sr-only">Cambiar navegación</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#page-top">AgendaVue</a>
      </div>

      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav navbar-right">
          <li class="hidden">
            <a href="#page-top"></a>
          </li>
          <li class="page-scroll">
            <a href="#contactos">Contactos</a>
          </li>
          <li class="page-scroll">
            <a href="#nuevo">Añadir contacto</a>
          </li>
          <li class="page-scroll">
            <a href="#about">Acerca de...</a>
          </li>
        </ul>
      </div>
      <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
  </nav>

  <!-- Header -->
  <header>
    <div class="container">
      <div class="row">
        <div class="col-lg-12">
          <div class="intro-text">
            <span class="name">AgendaVue</span>
            <hr class="star-light" />
            <span class="skills">Agenda básica en Vue</span>
          </div>
        </div>
      </div>
    </div>
  </header>

  <!-- Contactos Grid Section -->
  <section id="contactos">
    <div class="container">
      <div class="row">
        <div class="col-lg-12 text-center">
          <h2>Contactos</h2>
          <hr class="star-primary" />
        </div>
      </div>
      <div v-if="contacts.length > 0">
        <div class="row">
          <div class="col-xs-4">
            <h4>Nombre</h4>
          </div>
          <div class="col-xs-4">
            <h4>Email</h4>
          </div>
          <div class="col-xs-3">
            <h4>Teléfono</h4>
          </div>
        </div>
        <div class="row" v-for="contact in contacts" :key="contact.id">
          <div class="col-xs-4">
            <p>{{ contact.name }}</p>
          </div>
          <div class="col-xs-4">
            <p>{{ contact.email }}</p>
          </div>
          <div class="col-xs-3">
            <p>{{ contact.phone }}</p>
          </div>
          <div class="col-xs-1">
            <button class="btn btn-danger btn-sm" @click="deleteContact(contact.id)">
              <span class="glyphicon glyphicon-remove"></span>
            </button>
          </div>
        </div>
      </div>
      <div v-else>
        <div class="row">
          <div class="col-xs-12">
            <p>No se ha encontrado ningún contacto.</p>
          </div>
        </div>
      </div>
    </div>
  </section>

  <!-- Añadir nuevo Section -->
  <section id="nuevo">
    <div class="container">
      <div class="row">
        <div class="col-lg-12 text-center">
          <h2>Añadir contacto</h2>
          <hr class="star-primary" />
        </div>
      </div>
      <div class="row">
        <div class="col-lg-8 col-lg-offset-2">
          <form name="sentMessage" id="contactForm" novalidate>
            <div class="row control-group">
              <div class="form-group col-xs-12 floating-label-form-group controls">
                <label>Nombre</label>
                <input type="text" class="form-control" placeholder="Nombre" id="name" />
                <p class="help-block text-danger"></p>
              </div>
            </div>
            <div class="row control-group">
              <div class="form-group col-xs-12 floating-label-form-group controls">
                <label>Email</label>
                <input type="email" class="form-control" placeholder="Email" id="email" />
                <p class="help-block text-danger"></p>
              </div>
            </div>
            <div class="row control-group">
              <div class="form-group col-xs-12 floating-label-form-group controls">
                <label>Teléfono</label>
                <input
                  type="tel"
                  class="form-control"
                  placeholder="Número de teléfono"
                  id="phone"
                />
                <p class="help-block text-danger"></p>
              </div>
            </div>
            <br />
            <div id="success"></div>
            <div class="row">
              <div class="form-group col-xs-12">
                <button type="submit" class="btn btn-success btn-lg" id="btnSubmit">Añadir</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </section>

  <!-- About Section -->
  <section class="success" id="about">
    <div class="container">
      <div class="row">
        <div class="col-lg-12 text-center">
          <h2>Acerca de...</h2>
          <hr class="star-light" />
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12 text-center">
          <p>AgendaVue es un proyecto simple en VueJS.</p>
        </div>
      </div>
    </div>
  </section>

  <!-- Footer -->
  <footer class="text-center">
    <div class="footer-above">
      <div class="container"></div>
    </div>
    <div class="footer-below">
      <div class="container">
        <div class="row">
          <div class="col-lg-12">Copyleft &copy; e-ghost 2019</div>
        </div>
      </div>
    </div>
  </footer>

  <!-- Scroll to Top Button (Only visible on small and extra-small screen sizes) -->
  <div class="scroll-top page-scroll visible-xs visible-sm">
    <a class="btn btn-primary" href="#page-top">
      <i class="fa fa-chevron-up"></i>
    </a>
  </div>
</template>
