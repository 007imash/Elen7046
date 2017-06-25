import { UIGroup4ELEN7046Page } from './app.po';

describe('ui-group4-elen7046 App', () => {
  let page: UIGroup4ELEN7046Page;

  beforeEach(() => {
    page = new UIGroup4ELEN7046Page();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
