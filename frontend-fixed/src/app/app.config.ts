import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter ,withRouterConfig} from '@angular/router';
import { provideHttpClient ,withInterceptors} from '@angular/common/http';
import { jwtInterceptor } from './core/interceptors/jwt.interceptor';
import { routes } from './app.routes';
import { provideToastr } from 'ngx-toastr';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideCharts , withDefaultRegisterables} from 'ng2-charts';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes,withRouterConfig({ onSameUrlNavigation: 'reload' })),
    provideHttpClient(withInterceptors([jwtInterceptor])),
    provideToastr(),
    provideCharts(withDefaultRegisterables()),
    provideAnimations()

  ]
};
