// eslint-disable-next-line no-undef
module.exports = {
  ci: false,
  plugins: [
    '@semantic-release/commit-analyzer',
    '@semantic-release/release-notes-generator',
    '@semantic-release/changelog',
    ['@semantic-release/npm', { npmPublish: true, pkgRoot: '.' }],
    [
      '@semantic-release/exec',
      {
        execCwd: '.',
        prepareCmd:
          'echo "prepare" && git add CHANGELOG.md package.json package-lock.json && git commit -n -m "chore(release): ${nextRelease.version} [skip ci]\n\n${nextRelease.notes}" && git push',
      },
    ],
    '@semantic-release/git',
  ],
  branches: ['main'],
};
