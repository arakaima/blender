import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'expert',
        data: { pageTitle: 'Experts' },
        loadChildren: () => import('./expert/expert.module').then(m => m.ExpertModule),
      },
      {
        path: 'activity',
        data: { pageTitle: 'Activities' },
        loadChildren: () => import('./activity/activity.module').then(m => m.ActivityModule),
      },
      {
        path: 'inspection-type',
        data: { pageTitle: 'InspectionTypes' },
        loadChildren: () => import('./inspection-type/inspection-type.module').then(m => m.InspectionTypeModule),
      },
      {
        path: 'inspection',
        data: { pageTitle: 'Inspections' },
        loadChildren: () => import('./inspection/inspection.module').then(m => m.InspectionModule),
      },
      {
        path: 'risk-assessment',
        data: { pageTitle: 'RiskAssessments' },
        loadChildren: () => import('./risk-assessment/risk-assessment.module').then(m => m.RiskAssessmentModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'Locations' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'requests-message',
        data: { pageTitle: 'RequestsMessages' },
        loadChildren: () => import('./requests-message/requests-message.module').then(m => m.RequestsMessageModule),
      },
      {
        path: 'dossier',
        data: { pageTitle: 'Dossiers' },
        loadChildren: () => import('./dossier/dossier.module').then(m => m.DossierModule),
      },
      {
        path: 'inspector',
        data: { pageTitle: 'Inspectors' },
        loadChildren: () => import('./inspector/inspector.module').then(m => m.InspectorModule),
      },
      {
        path: 'attached-document',
        data: { pageTitle: 'AttachedDocuments' },
        loadChildren: () => import('./attached-document/attached-document.module').then(m => m.AttachedDocumentModule),
      },
      {
        path: 'message',
        data: { pageTitle: 'Messages' },
        loadChildren: () => import('./message/message.module').then(m => m.MessageModule),
      },
      {
        path: 'requests',
        data: { pageTitle: 'Requests' },
        loadChildren: () => import('./requests/requests.module').then(m => m.RequestsModule),
      },
      {
        path: 'site',
        data: { pageTitle: 'Sites' },
        loadChildren: () => import('./site/site.module').then(m => m.SiteModule),
      },
      {
        path: 'contact-person',
        data: { pageTitle: 'ContactPeople' },
        loadChildren: () => import('./contact-person/contact-person.module').then(m => m.ContactPersonModule),
      },
      {
        path: 'dossier-type',
        data: { pageTitle: 'DossierTypes' },
        loadChildren: () => import('./dossier-type/dossier-type.module').then(m => m.DossierTypeModule),
      },
      {
        path: 'deficiency',
        data: { pageTitle: 'Deficiencies' },
        loadChildren: () => import('./deficiency/deficiency.module').then(m => m.DeficiencyModule),
      },
      {
        path: 'dossier-status',
        data: { pageTitle: 'DossierStatuses' },
        loadChildren: () => import('./dossier-status/dossier-status.module').then(m => m.DossierStatusModule),
      },
      {
        path: 'organization',
        data: { pageTitle: 'Organizations' },
        loadChildren: () => import('./organization/organization.module').then(m => m.OrganizationModule),
      },
      {
        path: 'note',
        data: { pageTitle: 'Notes' },
        loadChildren: () => import('./note/note.module').then(m => m.NoteModule),
      },
      {
        path: 'inspector-dossier',
        data: { pageTitle: 'InspectorDossiers' },
        loadChildren: () => import('./inspector-dossier/inspector-dossier.module').then(m => m.InspectorDossierModule),
      },
      {
        path: 'capa-document',
        data: { pageTitle: 'CapaDocuments' },
        loadChildren: () => import('./capa-document/capa-document.module').then(m => m.CapaDocumentModule),
      },
      {
        path: 'inspection-report',
        data: { pageTitle: 'InspectionReports' },
        loadChildren: () => import('./inspection-report/inspection-report.module').then(m => m.InspectionReportModule),
      },
      {
        path: 'organization-document',
        data: { pageTitle: 'OrganizationDocuments' },
        loadChildren: () => import('./organization-document/organization-document.module').then(m => m.OrganizationDocumentModule),
      },
      {
        path: 'capa',
        data: { pageTitle: 'Capas' },
        loadChildren: () => import('./capa/capa.module').then(m => m.CapaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
