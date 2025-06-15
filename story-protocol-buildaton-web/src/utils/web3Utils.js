
import { ethers } from 'ethers';

export const CONTRACT_ABI = [
  {"inputs":[],"stateMutability":"nonpayable","type":"constructor"},
  {"inputs":[{"internalType":"address","name":"owner","type":"address"}],"name":"OwnableInvalidOwner","type":"error"},
  {"inputs":[{"internalType":"address","name":"account","type":"address"}],"name":"OwnableUnauthorizedAccount","type":"error"},
  {"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"previousOwner","type":"address"},{"indexed":true,"internalType":"address","name":"newOwner","type":"address"}],"name":"OwnershipTransferred","type":"event"},
  {"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"ipAssetId","type":"address"},{"indexed":false,"internalType":"uint256","name":"totalSlashedAmount","type":"uint256"}],"name":"Slashed","type":"event"},
  {"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"user","type":"address"},{"indexed":true,"internalType":"address","name":"ipAssetId","type":"address"},{"indexed":false,"internalType":"uint256","name":"amount","type":"uint256"}],"name":"Staked","type":"event"},
  {"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"user","type":"address"},{"indexed":true,"internalType":"address","name":"ipAssetId","type":"address"},{"indexed":false,"internalType":"uint256","name":"amount","type":"uint256"}],"name":"Unstaked","type":"event"},
  {"inputs":[{"internalType":"address","name":"ipAssetId","type":"address"}],"name":"getTotalStakedAmount","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},
  {"inputs":[{"internalType":"address","name":"user","type":"address"},{"internalType":"address","name":"ipAssetId","type":"address"}],"name":"getUserStakedAmount","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},
  {"inputs":[{"internalType":"address","name":"","type":"address"},{"internalType":"address","name":"","type":"address"}],"name":"hasStaked","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"view","type":"function"},
  {"inputs":[],"name":"owner","outputs":[{"internalType":"address","name":"","type":"address"}],"stateMutability":"view","type":"function"},
  {"inputs":[],"name":"renounceOwnership","outputs":[],"stateMutability":"nonpayable","type":"function"},
  {"inputs":[{"internalType":"address","name":"ipAssetId","type":"address"}],"name":"slash","outputs":[],"stateMutability":"nonpayable","type":"function"},
  {"inputs":[{"internalType":"address","name":"ipAssetId","type":"address"}],"name":"stake","outputs":[],"stateMutability":"payable","type":"function"},
  {"inputs":[{"internalType":"address","name":"","type":"address"},{"internalType":"uint256","name":"","type":"uint256"}],"name":"stakersPerIP","outputs":[{"internalType":"address","name":"","type":"address"}],"stateMutability":"view","type":"function"},
  {"inputs":[{"internalType":"address","name":"","type":"address"}],"name":"totalStakes","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},
  {"inputs":[{"internalType":"address","name":"newOwner","type":"address"}],"name":"transferOwnership","outputs":[],"stateMutability":"nonpayable","type":"function"},
  {"inputs":[{"internalType":"address","name":"ipAssetId","type":"address"},{"internalType":"uint256","name":"amount","type":"uint256"}],"name":"unstake","outputs":[],"stateMutability":"nonpayable","type":"function"},
  {"inputs":[{"internalType":"address","name":"","type":"address"},{"internalType":"address","name":"","type":"address"}],"name":"userStakes","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},
  {"stateMutability":"payable","type":"receive"}
];

export const CONTRACT_ADDRESS = "0x6E0D112252335D08DBFCD1dA187Bcc43cdA62a9B";


export const checkMetaMask = () => {
  if (typeof window.ethereum !== 'undefined') {
    return true;
  }
  alert('MetaMask is not installed. Please install MetaMask.');
  return false;
};


export const connectWallet = async () => {
  if (!checkMetaMask()) return null;
  
  try {
    const provider = new ethers.BrowserProvider(window.ethereum);
    await provider.send("eth_requestAccounts", []);
    const signer = await provider.getSigner();
    const address = await signer.getAddress();
    return address;
  } catch (error) {
    console.error('MetaMask connection failed:', error);
    throw new Error('Failed to connect to MetaMask.');
  }
};


export const checkAndSwitchNetwork = async () => {
  if (!checkMetaMask()) return false;
  
  const sepoliaChainId = '0xaa36a7';
  
  try {
    await window.ethereum.request({
      method: 'wallet_switchEthereumChain',
      params: [{ chainId: sepoliaChainId }],
    });
    return true;
  } catch (error) {
    if (error.code === 4902) {

      try {
        await window.ethereum.request({
          method: 'wallet_addEthereumChain',
          params: [{
            chainId: sepoliaChainId,
            chainName: 'Sepolia Testnet',
            rpcUrls: ['https://sepolia.infura.io/v3/'],
            nativeCurrency: {
              name: 'SepoliaETH',
              symbol: 'ETH',
              decimals: 18,
            },
            blockExplorerUrls: ['https://sepolia.etherscan.io/'],
          }],
        });
        return true;
      } catch (addError) {
        console.error('Failed to add network:', addError);
        return false;
      }
    }
    console.error('Failed to switch network:', error);
    return false;
  }
};


export const getContract = async () => {
  if (!checkMetaMask()) return null;
  
  try {
    const provider = new ethers.BrowserProvider(window.ethereum);
    const signer = await provider.getSigner();
    return new ethers.Contract(CONTRACT_ADDRESS, CONTRACT_ABI, signer);
  } catch (error) {
    console.error('Failed to load contract:', error);
    return null;
  }
};


export const stakeToAsset = async (ipAssetId, amount, userAccount) => {
  const networkSwitched = await checkAndSwitchNetwork();
  if (!networkSwitched) throw new Error('Please switch to Sepolia network.');
  
  const contract = await getContract();
  if (!contract) throw new Error('Failed to load contract.');
  
  try {
    const valueInWei = ethers.parseEther(amount.toString());
    
    const tx = await contract.stake(ipAssetId, {
      value: valueInWei
    });
    

    const receipt = await tx.wait();
    return receipt;
  } catch (error) {
    console.error('Stake failed:', error);
    throw error;
  }
};


export const unstakeFromAsset = async (ipAssetId, amount, userAccount) => {
  const networkSwitched = await checkAndSwitchNetwork();
  if (!networkSwitched) throw new Error('Please switch to Sepolia network.');
  
  const contract = await getContract();
  if (!contract) throw new Error('Failed to load contract.');
  
  try {
    const valueInWei = ethers.parseEther(amount.toString());
    
    const tx = await contract.unstake(ipAssetId, valueInWei);
    

    const receipt = await tx.wait();
    return receipt;
  } catch (error) {
    console.error('Unstake failed:', error);
    throw error;
  }
};


export const getUserStakedAmount = async (userAccount, ipAssetId) => {
  if (!checkMetaMask()) return '0';
  
  try {
    const provider = new ethers.BrowserProvider(window.ethereum);
    const contract = new ethers.Contract(CONTRACT_ADDRESS, CONTRACT_ABI, provider);
    
    const amount = await contract.getUserStakedAmount(userAccount, ipAssetId);
    return ethers.formatEther(amount);
  } catch (error) {
    console.error('Failed to fetch staking info:', error);
    return '0';
  }
};


export const slashAsset = async (ipAssetId) => {
  const networkSwitched = await checkAndSwitchNetwork();
  if (!networkSwitched) throw new Error('Please switch to Sepolia network.');
  
  const contract = await getContract();
  if (!contract) throw new Error('Failed to load contract.');
  
  try {
    const tx = await contract.slash(ipAssetId);
    

    const receipt = await tx.wait();
    return receipt;
  } catch (error) {
    console.error('Slash failed:', error);
    throw error;
  }
};
