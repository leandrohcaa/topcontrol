import { Component, ViewChild } from '@angular/core';
import { Nav, Platform, Tabs, ModalController } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { LoginPage } from '../pages/login/login';
import { CookPage } from '../pages/cook/cook';
import { PayPage } from '../pages/pay/pay';
import { ModalContact } from '../commons/modal/contact/index';
import { AuthenticationRepository } from '../repository/index';

@Component({
    templateUrl: 'app.html'
})
export class MyApp {
    rootPage: any = 'LoginPage';
    cookPage: any = CookPage;
    payPage: any = PayPage;

    @ViewChild(Nav) nav: Nav;
    @ViewChild("footerTabs") footerTabs: Tabs;
    pagesMenu: Array<{ title: string, component: any }>;

    constructor(public platform: Platform, public statusBar: StatusBar, public splashScreen: SplashScreen,
        public modalCtrl: ModalController, public authenticationRepository: AuthenticationRepository) {
        this.initializeApp();

        this.pagesMenu = [
            { title: 'Contato', component: ModalContact },
            { title: 'Sair', component: 'Sair' }
        ];
    }

    initializeApp() {
        this.platform.ready().then(() => {
            // Okay, so the platform is ready and our plugins are available.
            // Here you can do any higher level native things you might need.
            this.statusBar.styleDefault();
            this.splashScreen.hide();
        });
    }

    openPage(page) {
        if (page.component == 'Sair')
            this.logout();
        else if (page.component == ModalContact) {
            let modal = this.modalCtrl.create(ModalContact);
            modal.present();
        }
        else
            this.nav.setRoot(page.component);
    }

    onTabChange() {
        if (this.footerTabs.getSelected() != null)
            this.nav.setRoot(this.footerTabs.getSelected().root);
    }

    isLogged() {
        if (this.authenticationRepository.getUser() != null)
            return true;
        return false;
    }

    getUserLogged() {
        return this.authenticationRepository.getUser();
    }

    logout() {
        this.authenticationRepository.logout();
        this.nav.setRoot(LoginPage);
    }
}