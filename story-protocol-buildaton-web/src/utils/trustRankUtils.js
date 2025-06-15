
export const getTrustRankConfig = (rank) => {
  const configs = {
    DIAMOND: {
      name: 'Diamond',
      emoji: '💎',
      color: '#00d4ff',
      gradient: 'linear-gradient(135deg, #00d4ff 0%, #0099cc 100%)',
      description: 'Exceptional Trust'
    },
    PLATINUM: {
      name: 'Platinum',
      emoji: '🏆',
      color: '#5e8b57',
      gradient: 'linear-gradient(135deg, #5e8b57 0%, #2e8b57 100%)',
      description: 'High Trust'
    },
    GOLD: {
      name: 'Gold',
      emoji: '🥇',
      color: '#ffd700',
      gradient: 'linear-gradient(135deg, #ffd700 0%, #ffb347 100%)',
      description: 'Good Trust'
    },
    SILVER: {
      name: 'Silver',
      emoji: '🥈',
      color: '#c0c0c0',
      gradient: 'linear-gradient(135deg, #c0c0c0 0%, #a8a8a8 100%)',
      description: 'Fair Trust'
    },
    BRONZE: {
      name: 'Bronze',
      emoji: '🥉',
      color: '#cd7f32',
      gradient: 'linear-gradient(135deg, #cd7f32 0%, #a0522d 100%)',
      description: 'Basic Trust'
    }
  };
  
  return configs[rank] || configs.BRONZE;
};

export const formatTrustRankValue = (trustRankValue) => {

  return trustRankValue || 'BRONZE';
};
